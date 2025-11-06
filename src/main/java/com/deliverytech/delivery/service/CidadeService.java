package com.deliverytech.delivery.service;

import java.io.*;
import java.util.*;

import org.springframework.core.io.*;
import org.springframework.stereotype.*;

import com.deliverytech.delivery.domain.model.*;
import com.deliverytech.delivery.domain.repository.*;
import com.fasterxml.jackson.core.type.*;
import com.fasterxml.jackson.databind.*;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.*;
import jakarta.persistence.*;
import lombok.*;

@Service
@RequiredArgsConstructor
@Transactional
public class CidadeService {

    private final CidadeRepository cidadeRepository;
    private final EstadoRepository estadoRepository;

    private Map<String, List<String>> cidadesMap;

    @PostConstruct
    public void carregarCidades() {
        try (InputStream in = new ClassPathResource("cidades.json").getInputStream()) {
            ObjectMapper mapper = new ObjectMapper();

            Map<String, List<String>> temp = mapper.readValue(
                    in, new TypeReference<Map<String, List<String>>>() {
            }
            );

            Map<String, List<String>> normalizado = new HashMap<>();
            for (Map.Entry<String, List<String>> e : temp.entrySet()) {
                String uf = e.getKey().trim().toUpperCase();
                List<String> nomes = e.getValue().stream()
                        .filter(Objects::nonNull)
                        .map(s -> s.trim())
                        .filter(s -> !s.isEmpty())
                        .toList();
                normalizado.put(uf, nomes);
            }

            this.cidadesMap = Collections.unmodifiableMap(normalizado);

        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar cidades.json", e);
        }
    }

    public Cidade salvar(String nomeCidade, String uf) {
        String ufNorm = normalizarUf(uf);
        if (!cidadeRepository.existeCidadeNaUf(ufNorm, nomeCidade)) {
            throw new IllegalArgumentException("Cidade não reconhecida para a UF: " + ufNorm);
        }

        Estado estado = estadoRepository.findByUfIgnoreCase(ufNorm)
                .orElseThrow(() -> new EntityNotFoundException("Estado não encontrado: " + ufNorm));

        cidadeRepository.findByNomeIgnoreCaseAndEstadoUfIgnoreCase(nomeCidade, ufNorm)
                .ifPresent(c -> {
                    throw new IllegalArgumentException("Cidade já cadastrada para essa UF");
                });

        var cidade = Cidade.builder()
                .nome(nomeCidade.trim())
                .estado(estado)
                .build();

        return cidadeRepository.save(cidade);
    }

    public Cidade atualizar(Long id, Cidade cidadeAtualizada) {
        Cidade existente = buscarPorId(id);
        existente.setNome(cidadeAtualizada.getNome());

        if (cidadeAtualizada.getEstado() != null) {
            Estado estado = estadoRepository.findById(cidadeAtualizada.getEstado().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Estado não encontrado para a cidade informada."));
            existente.setEstado(estado);
        }

        return cidadeRepository.save(existente);
    }

    public void deletar(Long id) {
        Cidade cidade = buscarPorId(id);
        cidadeRepository.delete(cidade);
    }

    @Transactional(readOnly = true)
    public List<Cidade> listarTodos() {
        return cidadeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Cidade buscarPorId(Long id) {
        return cidadeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cidade não encontrada com ID: " + id));
    }

    // @Transactional(readOnly = true)
    // public boolean existeCidadeNaUf(String uf, String nomeCidade) {
    //     if (uf == null || nomeCidade == null) {
    //         return false;
    //     }
    //     List<String> lista = cidadesMap.get(uf.trim().toUpperCase());
    //     if (lista == null) {
    //         return false;
    //     }
    //     String alvo = nomeCidade.trim();
    //     return lista.stream().anyMatch(c -> c.equalsIgnoreCase(alvo));
    // }

    @Transactional(readOnly = true)
    public List<String> listarPorUf(String uf) {
        if (uf == null) {
            return List.of();
        }
        return cidadesMap.getOrDefault(uf.trim().toUpperCase(), List.of());
    }

    public String normalizarUf(String uf) {
        return uf == null ? null : uf.trim().toUpperCase();
    }
}
