package com.deliverytech.delivery.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CidadeRequest(
        @NotBlank(message = "O nome da cidade é obrigatório")
        String nome,
        
        @NotNull(message = "Id do estado é obrigatório")
        Long estadoId) {

}
