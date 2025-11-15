package com.deliverytech.delivery.domain.validator;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.deliverytech.delivery.api.exceptions.EntityNotFoundException;
import com.deliverytech.delivery.domain.model.Endereco;
import com.deliverytech.delivery.domain.repository.EnderecoRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class EnderecoValidator {

    private final EnderecoRepository enderecoRepository;

    public Endereco validarEndereco(Long enderecoId) {

        return enderecoRepository.findById(Objects.requireNonNull(enderecoId))
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado com ID: " + enderecoId));
    }
}
