package com.deliverytech.delivery.api.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.deliverytech.delivery.domain.model.Estado;

public record EstadoResponse(

        long id,
        String nome,
        String uf,
        List<String> cidades) {

    public static EstadoResponse of(Estado e) {
        List<String> cidades = e.getCidades() != null
                ? e.getCidades().stream()
                        .map(c -> c.getNome())
                        .collect(Collectors.toList())
                : List.of();

        return new EstadoResponse(
                e.getId(),
                e.getNome(),
                e.getUf(),
                cidades);
    }
}
