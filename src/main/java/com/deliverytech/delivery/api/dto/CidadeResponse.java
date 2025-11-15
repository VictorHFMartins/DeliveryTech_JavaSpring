package com.deliverytech.delivery.api.dto;

import com.deliverytech.delivery.domain.model.Cidade;

public record CidadeResponse(
        Long id,
        String nome,
        EstadoResponse estado) {

    public static CidadeResponse of(Cidade c) {

        return new CidadeResponse(
                c.getId(),
                c.getNome(),
                EstadoResponse.of(c.getEstado()));
    }
}
