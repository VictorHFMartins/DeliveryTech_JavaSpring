package com.deliverytech.delivery.api.dto;

import com.deliverytech.delivery.domain.enums.TipoLogradouro;
import com.deliverytech.delivery.domain.model.Endereco;

public record EnderecoRequest(
        Long id,
        String nome,
        TipoLogradouro Logradouro,
        String numero,
        String bairro,
        String complemento,
        String cep,
        String cidade,
        String uf,
        String estado
        ) {

    public static EnderecoRequest of(Endereco e) {
        var cep = e.getCep();
        var cidade = cep != null ? cep.getCidade() : null;
        var estado = cidade != null ? cidade.getEstado() : null;

        return new EnderecoRequest(
                e.getId(),
                e.getNome(),
                e.getLogradouro(),
                e.getNumero(),
                e.getBairro(),
                e.getComplemento(),
                cep != null ? cep.getCodigo() : null,
                cidade != null ? cidade.getNome() : null,
                estado != null ? estado.getUf() : null,
                estado != null ? estado.getNome() : null
        );

    }
}
