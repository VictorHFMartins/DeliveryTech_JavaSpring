package com.deliverytech.delivery.domain.services;

import java.util.List;

import com.deliverytech.delivery.api.dto.EstadoRequest;
import com.deliverytech.delivery.api.dto.EstadoResponse;
import com.deliverytech.delivery.domain.model.Estado;

public interface EstadoService {

    Estado buscarOuCriar(EstadoRequest dto);

    EstadoResponse criar(EstadoRequest estadoDto);

    EstadoResponse alterar(Long idEstado, EstadoRequest estadoDto);

    EstadoResponse buscarPorUf(String ufEstado);

    EstadoResponse buscarPorCidade(String nomeCidade);

    List<EstadoResponse> listarPorNomeContendo(String nomeEstado);

}
