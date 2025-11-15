package com.deliverytech.delivery.api.dto;

import com.deliverytech.delivery.domain.model.Cep;

public record CepResponse(
        long id,
        String codigo,
        CidadeResponse cidade) {

    public static CepResponse of(Cep c) {

        return new CepResponse(
                c.getId(),
                c.getCodigo(),
                CidadeResponse.of(c.getCidade()));
    }
}
