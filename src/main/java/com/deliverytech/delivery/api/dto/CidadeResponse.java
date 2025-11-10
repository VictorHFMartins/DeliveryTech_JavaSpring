package com.deliverytech.delivery.api.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.deliverytech.delivery.domain.model.Cidade;

public record CidadeResponse(
        Long id,
        String nome,
        EstadoResponse estado,
        List<String> ceps) {
    public static CidadeResponse of(Cidade c) {
        List<String> ceps = c.getCeps() != null
                ? c.getCeps().stream()
                        .map(ce -> ce.getCodigo())
                        .collect(Collectors.toList())
                : List.of();

        return new CidadeResponse(
                c.getId(),
                c.getNome(),
                EstadoResponse.of(c.getEstado()),
                ceps);
    }
}
