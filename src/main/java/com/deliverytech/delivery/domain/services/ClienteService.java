package com.deliverytech.delivery.domain.services;

import java.util.List;

import com.deliverytech.delivery.api.dto.ClienteRequest;
import com.deliverytech.delivery.api.dto.ClienteResponse;

public interface ClienteService {

    ClienteResponse criar(ClienteRequest cliente);

    ClienteResponse alterar(Long id, ClienteRequest novoCliente);

    ClienteResponse buscarPorId(long id);

    ClienteResponse buscarPorEmail(String email);

    ClienteResponse ativarDesativar(Long clienteId);

    List<ClienteResponse> listarPorNomeContendo(String nome);

    List<ClienteResponse> listarPorStatusAtivo();

    List<ClienteResponse> listarPorCep(String nome);

    List<ClienteResponse> listarPorCidade(String nome);

    List<ClienteResponse> listarPorNomeEstado(String nome);

    List<ClienteResponse> listarPorTelefoneNum(String nome);

}
