package com.deliverytech.delivery.api.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.deliverytech.delivery.domain.model.Cep;

public record CepResponse(
        long id,
        String codigo,
        CidadeResponse cidade,
        List<Long> enderecosId) {
    public static CepResponse of(Cep c) {
        List<Long> enderecos = c.getEnderecos() != null
                ? c.getEnderecos().stream()
                        .map(e -> e.getId())
                        .collect(Collectors.toList())
                : List.of();
        return new CepResponse(
                c.getId(),
                c.getCodigo(),
                CidadeResponse.of(c.getCidade()),
                enderecos);
    }
}
