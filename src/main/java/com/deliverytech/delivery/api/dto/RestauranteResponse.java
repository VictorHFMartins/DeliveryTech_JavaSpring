package com.deliverytech.delivery.api.dto;

import java.math.BigDecimal;
import java.time.LocalTime;

import com.deliverytech.delivery.domain.enums.CategoriaRestaurante;
import com.deliverytech.delivery.domain.enums.EstadoRestaurante;
import com.deliverytech.delivery.domain.model.Restaurante;

public record RestauranteResponse(
        Long id,
        String nome,
        String cnpj,
        String email,
        boolean status,
        EnderecoResponse endereco,
        CategoriaRestaurante classe,
        EstadoRestaurante estado,
        LocalTime horarioAbertura,
        LocalTime horarioFechamento,
        BigDecimal taxaEntrega
        ) {

    public static RestauranteResponse of(Restaurante r) {
        return new RestauranteResponse(
                r.getId(),
                r.getNome(),
                r.getCnpj(),
                r.getEmail(),
                r.isStatus(),
                EnderecoResponse.of(r.getEndereco()),
                r.getCategoria(),
                r.getEstado(),
                r.getHorarioAbertura(),
                r.getHorarioFechamento(),
                r.getTaxaEntrega()
        );
    }

}
