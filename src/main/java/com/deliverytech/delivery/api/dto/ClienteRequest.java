package com.deliverytech.delivery.api.dto;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClienteRequest(
        @NotBlank(message = "O nome é obrigatório") 
        String nome,

        @NotBlank(message = "Email é obrigatório") 
        @Email(message = "E-mail inválido")
        String email,
        
        @NotNull(message = "O id do telefone é obrigatório") 
        List<Long> telefoneIds,
        
        @NotNull(message = "Endereço é obrigatório") 
        Long enderecoId) {

}
