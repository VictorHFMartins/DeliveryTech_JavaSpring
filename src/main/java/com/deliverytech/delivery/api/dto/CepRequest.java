package com.deliverytech.delivery.api.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CepRequest(
        @NotBlank(message = "Cep é obrigatório")
        @Pattern(regexp = "\\d{5}-?\\d{3}", message = "Formato de CEP inválido (ex: 12345-678)")
        String codigo,
        
        @NotNull(message = "Id da cidade é obrigatório")
        Long cidadeId,
        
        List<Long> enderecosId) {
}