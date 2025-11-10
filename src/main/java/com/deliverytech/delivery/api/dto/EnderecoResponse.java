package com.deliverytech.delivery.api.dto;

import com.deliverytech.delivery.domain.enums.TipoLogradouro;
import com.deliverytech.delivery.domain.model.Endereco;

public record EnderecoResponse(
        Long id,
        TipoLogradouro tipoLogradouro,
        String logradouro,
        String numero,
        String bairro,
        String complemento,
        CepResponse cep) {

    public static EnderecoResponse of(Endereco e) {
        var cep = e.getCep();

        return new EnderecoResponse(
                e.getId(),
                e.getTipoLogradouro(),
                e.getLogradouro(),
                e.getNumero(),
                e.getBairro(),
                e.getComplemento(),
                CepResponse.of(cep));
    }
}
