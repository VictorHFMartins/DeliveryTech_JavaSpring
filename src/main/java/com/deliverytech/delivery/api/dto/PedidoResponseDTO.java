package com.deliverytech.delivery.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.deliverytech.delivery.domain.enums.StatusPedido;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PedidoResponseDTO {

    private Long id;
    private String numeroPedido;
    private LocalDateTime dataPedido;
    private StatusPedido status;
    private BigDecimal valorTotal;
    private String observacoes;
    private Long clienteId;
    private String clienteNome;
    private Long restauranteId;
    private String restauranteNome;
}
