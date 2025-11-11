package com.deliverytech.delivery.domain.services;

import java.util.List;

import com.deliverytech.delivery.api.dto.CidadeRequest;
import com.deliverytech.delivery.api.dto.CidadeResponse;

public interface CidadeService {

    CidadeResponse criar(CidadeRequest cidade);

    CidadeResponse alterar(long id, CidadeRequest cidade);

    CidadeResponse buscarPorId(Long id);

    CidadeResponse buscarCidadePorEstado(String nome, String estadoUf);

    List<CidadeResponse> buscarPorNomeContendo(String nome);

    List<CidadeResponse> buscarCidadesPorEstadoUf(String estadoUf);

    List<CidadeResponse> buscarPorCepCodigoContendo(String cepCodigo);

}
