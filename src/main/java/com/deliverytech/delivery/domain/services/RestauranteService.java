package com.deliverytech.delivery.domain.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.deliverytech.delivery.api.dto.ClienteResponse;
import com.deliverytech.delivery.api.dto.RestauranteRequest;
import com.deliverytech.delivery.api.dto.RestauranteResponse;
import com.deliverytech.delivery.domain.enums.CategoriaRestaurante;

public interface RestauranteService {

    RestauranteResponse adicionar(Long usuarioId, RestauranteRequest restauranteDto);

    RestauranteResponse alterar(Long idRestaurante, RestauranteRequest restauranteDto);

    RestauranteResponse buscarPorCnpj(String cnpj);

    RestauranteResponse buscarPorId(Long idRestaurante);

    RestauranteResponse buscarPorEmail(String emailRestaurante);

    List<RestauranteResponse> listarPorRankingTop5();

    List<ClienteResponse> listarPorTelefoneNum(String numeroTelefone);

    List<RestauranteResponse> listarPorTaxaDeEntrega(BigDecimal taxaEntrega);

    List<RestauranteResponse> listarPorNomeContaining(String nomeRestaurante);

    List<RestauranteResponse> listarPorHorarioDeAbertura(LocalDate horarioAbertura);

    List<RestauranteResponse> listarPorHorarioDeFechamento(LocalDate horarioFechamento);

    List<RestauranteResponse> listarPorCategoria(CategoriaRestaurante categoriaRestaurante);

}
