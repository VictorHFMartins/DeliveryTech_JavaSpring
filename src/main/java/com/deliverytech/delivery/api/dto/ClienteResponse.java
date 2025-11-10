package com.deliverytech.delivery.api.dto;

import com.deliverytech.delivery.domain.model.Cliente;

public record ClienteResponse(
        Long id,
        String nome,
        String email,
        boolean status,
        EnderecoResponse endereco) {

    public static ClienteResponse of(Cliente c) {
        return new ClienteResponse(
                c.getId(),
                c.getNome(),
                c.getEmail(),
                c.isStatus(),
                c.getEndereco() != null ? EnderecoResponse.of(c.getEndereco()) : null);
    }
}
