package com.deliverytech.delivery.api.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreatePedidoRequest {

    @NotNull(message = "O ID do cliente é obrigatório")
    private Long clienteId;

    @NotNull(message = "O ID do restaurante é obrigatório")
    private Long restauranteId;

    private String observacoes;

    @DecimalMin(value = "0.00", message = "O valor total deve ser maior ou igual a zero")
    private BigDecimal valorTotal;
}
