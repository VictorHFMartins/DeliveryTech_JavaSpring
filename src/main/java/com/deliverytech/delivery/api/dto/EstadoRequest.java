package com.deliverytech.delivery.api.dto;

import jakarta.validation.constraints.NotBlank;

public record EstadoRequest(
        @NotBlank(message = "O nome é obrigatório") 
        String nome,

        @NotBlank(message = "O uf é obrigatório") 
        String uf) {

}
