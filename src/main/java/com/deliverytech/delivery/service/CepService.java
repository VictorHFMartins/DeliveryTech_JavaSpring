package com.deliverytech.delivery.service;

import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery.domain.model.Cidade;
import com.deliverytech.delivery.domain.model.Estado;
import com.deliverytech.delivery.domain.repository.CidadeRepository;
import com.deliverytech.delivery.domain.repository.EstadoRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CepService {

    private Map<String, Map<String, List<String>>> cepMap;
    private final CidadeRepository cidadeRepository;
    private final EstadoRepository estadoRepository;

    @PostConstruct
    public void carregarCep() {
        try (InputStream in = new ClassPathResource("ceps.json").getInputStream()) {
            ObjectMapper mapper = new ObjectMapper();

            Map<String, Map<String, List<String>>> bruto
                    = mapper.readValue(in, new TypeReference<Map<String, Map<String, List<String>>>>() {
                    });

            // Normaliza chaves de UF e nomes de cidades (maiúsculas, sem acento, trim)
            this.cepMap = bruto.entrySet().stream().collect(
                    java.util.stream.Collectors.toUnmodifiableMap(
                            ufEntry -> ufEntry.getKey().trim().toUpperCase(Locale.ROOT),
                            ufEntry -> ufEntry.getValue().entrySet().stream().collect(
                                    java.util.stream.Collectors.toUnmodifiableMap(
                                            cityEntry -> normalizarCidade(cityEntry.getKey()),
                                            cityEntry -> cityEntry.getValue().stream()
                                                    .map(this::normalizarCep)
                                                    .toList()
                                    )
                            )
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar ceps.json", e);
        }
    }

    public Cidade salvar(String codigoCep, String uf) {
        String ufNorm = normalizarUf(uf);
        if (!cidadeRepository.existeCidadeNaUf(ufNorm, codigoCep)) {
            throw new IllegalArgumentException("Cidade não reconhecida para a UF: " + ufNorm);
        }

        Estado estado = estadoRepository.findByUfIgnoreCase(ufNorm)
                .orElseThrow(() -> new EntityNotFoundException("Estado não encontrado: " + ufNorm));

        cidadeRepository.findByNomeIgnoreCaseAndEstadoUfIgnoreCase(codigoCep, ufNorm)
                .ifPresent(c -> {
                    throw new IllegalArgumentException("Cidade já cadastrada para essa UF");
                });

        var cidade = Cidade.builder()
                .nome(codigoCep.trim())
                .estado(estado)
                .build();

        return cidadeRepository.save(cidade);
    }

    public String normalizarUf(String uf) {
        return uf == null ? null : uf.trim().toUpperCase();
    }

    public boolean validarCep(String uf, String cidade, String cep) {
        if (uf == null || cidade == null || cep == null) {
            return false;
        }

        String ufKey = uf.trim().toUpperCase(Locale.ROOT);
        String cidadeKey = normalizarCidade(cidade);
        String cepKey = normalizarCep(cep);

        Map<String, List<String>> cidades = cepMap.get(ufKey);
        if (cidades == null) {
            return false;
        }

        List<String> ceps = cidades.get(cidadeKey);
        if (ceps == null) {
            return false;
        }

        return ceps.contains(cepKey);
    }

    public List<String> listarPorCidade(String uf, String cidade) {
        if (uf == null || cidade == null) {
            return List.of();
        }
        Map<String, List<String>> cidades = cepMap.get(uf.trim().toUpperCase(Locale.ROOT));
        if (cidades == null) {
            return List.of();
        }
        return cidades.getOrDefault(normalizarCidade(cidade), List.of());
    }

    public List<String> listarCidadesPorUf(String uf) {
        if (uf == null) {
            return List.of();
        }
        Map<String, List<String>> cidades = cepMap.get(uf.trim().toUpperCase(Locale.ROOT));
        if (cidades == null) {
            return List.of();
        }
        return cidades.keySet().stream().sorted().toList();
    }

    public Map<String, Map<String, List<String>>> dump() {
        return Collections.unmodifiableMap(cepMap);
    }

    private String normalizarCidade(String nome) {
        if (nome == null) {
            throw new IllegalArgumentException("Nome da cidade não pode estar vazio");
        }
        String s = Normalizer.normalize(nome.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return s.toUpperCase(Locale.ROOT);
    }

    private String normalizarCep(String cep) {
        if (cep == null) {
            throw new IllegalArgumentException("Cep não pode estar vazio");
        }
        String digits = cep.replaceAll("\\D", "");
        if (digits.length() != 8) {
            throw new IllegalArgumentException("Cep Inválido");

        }
        return digits.substring(0, 5) + "-" + digits.substring(5);
    }

}
