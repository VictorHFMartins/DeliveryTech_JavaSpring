package com.deliverytech.delivery.api.dto;

import com.deliverytech.delivery.domain.model.Estado;

public record EstadoResponse(
        long id,
        String nome,
        String uf) {

    public static EstadoResponse of(Estado e) {

        return new EstadoResponse(
                e.getId(),
                e.getNome(),
                e.getUf());
    }
}
