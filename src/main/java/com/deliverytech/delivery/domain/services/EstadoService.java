package com.deliverytech.delivery.domain.services;

import java.util.List;

import com.deliverytech.delivery.api.dto.EstadoRequest;
import com.deliverytech.delivery.api.dto.EstadoResponse;

public interface EstadoService {

    EstadoResponse criar(EstadoRequest estado);

    EstadoResponse alterar(Long id, EstadoRequest estado);

    EstadoResponse buscarPorUf(String Uf);

    EstadoResponse buscarPorCidade(String nomeCidade);

    List<EstadoResponse> listarPorNomeContendo(String nome);

}
