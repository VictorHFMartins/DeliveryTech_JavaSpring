package com.deliverytech.delivery.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery.domain.model.Cliente;
import com.deliverytech.delivery.domain.model.Telefone;
import com.deliverytech.delivery.domain.repository.ClienteRepository;
import com.deliverytech.delivery.domain.repository.TelefoneRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TelefoneService {

    private final TelefoneRepository telefoneRepository;
    private final ClienteRepository clienteRepository;

    public Telefone adicionarParaCliente(Long clienteId, Telefone telefone) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        normalizar(telefone);
        validarBasico(telefone);

        if (telefoneRepository.existsByClienteIdAndDddAndNumero(clienteId, telefone.getDdd(), telefone.getNumero())) {
            throw new IllegalArgumentException("Telefone já existe");
        }

        telefone.setCliente(cliente);
        if (telefone.isAtivo() == false) {
            telefone.setAtivo(true);
        }

        return telefoneRepository.save(telefone);
    }

    private void validarBasico(Telefone t) {
        if (t.getPais() == null || t.getPais().length() != 2) {
            throw new IllegalArgumentException("Pais inválido (esperado 2 dígitos).");
        }
        if (t.getDdd() == null || t.getDdd().length() != 2) {
            throw new IllegalArgumentException("DDD inválido (esperado 2 dígitos).");
        }
        if (t.getNumero() == null || t.getNumero().length() < 8 || t.getNumero().length() > 9) {
            throw new IllegalArgumentException("Número inválido (8 ou 9 dígitos).");
        }
    }

    // Remove caracteres não numéricos e padroniza
    private void normalizar(Telefone t) {
        if (t.getPais() != null) {
            t.setPais(t.getPais().replaceAll("\\D", ""));
        }
        if (t.getDdd() != null) {
            t.setDdd(t.getDdd().replaceAll("\\D", ""));
        }
        if (t.getNumero() != null) {
            t.setNumero(t.getNumero().replaceAll("\\D", ""));
        }
    }

    public Telefone atualiazer(Telefone novoTelefone) {
        return novoTelefone;
    }

    @Transactional(readOnly = true)
    public Optional<Telefone> buscarPorId(Long id) {
        return telefoneRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Telefone> BuscarPorClienteId(Long clienteId) {
        return telefoneRepository.findByClienteId(clienteId);
    }
}
