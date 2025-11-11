package com.deliverytech.delivery.domain.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.deliverytech.delivery.api.dto.RestauranteRequest;
import com.deliverytech.delivery.api.dto.RestauranteResponse;
import com.deliverytech.delivery.domain.enums.CategoriaRestaurante;

public interface RestauranteService {

    RestauranteResponse criar(RestauranteRequest restaurante);

    RestauranteResponse alterar(Long id, RestauranteRequest novoRestaurante);

    RestauranteResponse buscarPorId(Long id);

    RestauranteResponse buscarPorCnpj(String cnpj);

    List<RestauranteResponse> listarPorRankingTop5();

    List<RestauranteResponse> listarPorNomeContaining(String nome);

    List<RestauranteResponse> listarPorTaxaDeEntrega(BigDecimal taxa);

    List<RestauranteResponse> listarPorCategoria(CategoriaRestaurante categoria);

    List<RestauranteResponse> listarPorHorarioDeAbertura(LocalDate horarioAbertura);

    List<RestauranteResponse> listarPorHorarioDeFechamento(LocalDate horarioFechamento);

}
