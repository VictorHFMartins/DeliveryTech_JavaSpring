package com.deliverytech.delivery.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery.domain.model.Cep;
import com.deliverytech.delivery.domain.model.Endereco;
import com.deliverytech.delivery.domain.repository.CepRepository;
import com.deliverytech.delivery.domain.repository.EnderecoRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;
    private final CepRepository cepRepository;

    @Transactional(readOnly = true)
    public List<Endereco> listarTodos() {
        return enderecoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Endereco buscarPorId(Long id) {
        return enderecoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado com ID: " + id));
    }

    @Transactional
    public Endereco salvar(Endereco endereco) {
        Cep cep = cepRepository.findById(endereco.getCep().getId())
                .orElseThrow(() -> new EntityNotFoundException("CEP não encontrado para o endereço informado."));
        endereco.setCep(cep);
        return enderecoRepository.save(endereco);
    }

    @Transactional
    public Endereco atualizar(Long id, Endereco enderecoAtualizado) {
        Endereco existente = buscarPorId(id);
        existente.setLogradouro(enderecoAtualizado.getLogradouro());
        existente.setNumero(enderecoAtualizado.getNumero());
        existente.setComplemento(enderecoAtualizado.getComplemento());
        existente.setBairro(enderecoAtualizado.getBairro());
        existente.setLogradouro(enderecoAtualizado.getLogradouro());

        if (enderecoAtualizado.getCep() != null) {
            Cep cep = cepRepository.findById(enderecoAtualizado.getCep().getId())
                    .orElseThrow(() -> new EntityNotFoundException("CEP não encontrado para o endereço informado."));
            existente.setCep(cep);
        }

        return enderecoRepository.save(existente);
    }

    @Transactional
    public void deletar(Long id) {
        Endereco endereco = buscarPorId(id);
        enderecoRepository.delete(endereco);
    }
}
