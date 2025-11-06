package com.deliverytech.delivery.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery.domain.model.Estado;
import com.deliverytech.delivery.domain.repository.EstadoRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EstadoService {

    private final EstadoRepository estadoRepository;

    private Map<String, String> estadosMap;

    @PostConstruct
    public void carregarEstados() {
        try (InputStream inputStream = new ClassPathResource("estados.json").getInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            List<Estado> estados = mapper.readValue(inputStream, new TypeReference<List<Estado>>() {
            });

            Map<String, String> tempMap = new HashMap<>();
            for (Estado e : estados) {
                if (e.getUf() != null && e.getNome() != null) {
                    tempMap.put(e.getUf().trim().toUpperCase(), e.getNome().trim());
                }

            }
            this.estadosMap = Collections.unmodifiableMap(tempMap);

        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar estados.json", e);
        }
    }

    @Transactional(readOnly = true)
    public Map<String, String> listarTodos() {
        return estadosMap;
    }

    @Transactional(readOnly = true)
    public Estado buscarPorId(Long id) {
        return estadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estado não encontrado com ID: " + id));
    }

    @Transactional
    public Estado salvar(Estado estado) {
        return estadoRepository.save(estado);
    }

    @Transactional
    public Estado atualizar(Long id, Estado estadoAtualizado) {
        Estado existente = buscarPorId(id);
        existente.setNome(estadoAtualizado.getNome());
        existente.setUf(estadoAtualizado.getUf());
        return estadoRepository.save(existente);
    }

    @Transactional
    public void deletar(Long id) {
        Estado estado = buscarPorId(id);
        estadoRepository.delete(estado);
    }

    public void validarEstado(Estado estado) {
        if (estado.getUf() == null || estado.getUf().trim().length() < 2) {
            throw new IllegalArgumentException("O estado é obrigatório e deve ter pelo menos 2 caracteres");
        }

        if (estado.getNome() == null || estado.getNome().trim().length() < 2) {
            throw new IllegalArgumentException("O estado é obrigatório e deve ter pelo menos 2 caracteres");
        }

        String uf = estado.getUf().trim().toUpperCase();
        String nome = estado.getNome().trim();

        if (!validarUf(uf)) {
            throw new IllegalArgumentException("UF inválido: " + uf);
        }

        if (!nome.equalsIgnoreCase(estadosMap.get(uf))) {
            throw new IllegalArgumentException("Nome do estado inválido" + nome);
        }
    }

    public boolean validarUf(String uf) {
        return uf != null && estadosMap.containsKey(uf.toUpperCase());
    }

    public String buscarNomePorUf(String Uf) {
        if (Uf == null) {
            return null;
        }
        return estadosMap.get(Uf.toUpperCase());
    }

}
