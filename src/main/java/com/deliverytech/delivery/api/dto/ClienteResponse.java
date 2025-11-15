package com.deliverytech.delivery.api.dto;

import java.util.List;

import com.deliverytech.delivery.domain.model.Cliente;
import com.deliverytech.delivery.domain.model.Telefone;

public record ClienteResponse(
        Long id,
        String nome,
        String email,
        boolean status,
        List<String> telefones,
        EnderecoResponse endereco) {

    public static ClienteResponse of(Cliente c) {

        List<String> telefones = c.getTelefones().stream()
                .filter(t -> t.getUsuario() != null && t.getUsuario().getId().equals(c.getId()))
                .map(Telefone::getNumero)
                .toList();

        return new ClienteResponse(
                c.getId(),
                c.getNome(),
                c.getEmail(),
                c.isStatus(),
                telefones,
                c.getEndereco() != null ? EnderecoResponse.of(c.getEndereco()) : null
        );
    }
}
