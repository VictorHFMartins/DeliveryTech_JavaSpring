package com.deliverytech.delivery.domain.services;

import java.util.List;

import com.deliverytech.delivery.api.dto.CepRequest;
import com.deliverytech.delivery.api.dto.CepResponse;

public interface CepService {

    CepResponse criar(CepRequest cep);

    CepResponse alterar(Long id, CepRequest novoCep);

    CepResponse buscarCepPorId(Long id);

    CepResponse buscarCepPorCodigo(String codigo);

    List<CepResponse> listarCepsPorCidadeNome(String nomeCidade
    );

}
