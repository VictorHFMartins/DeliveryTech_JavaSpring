package com.deliverytech.delivery.domain.services;

import java.util.List;

import com.deliverytech.delivery.api.dto.ClienteRequest;
import com.deliverytech.delivery.api.dto.ClienteResponse;

public interface ClienteService {

    void ativarDesativar(Long clienteId);

    ClienteResponse criar(ClienteRequest clienteDto);

    ClienteResponse atualizar(Long id, ClienteRequest clienteDto);

    ClienteResponse buscarPorId(long idCliente);

    ClienteResponse buscarPorEmail(String emailCliente);

    List<ClienteResponse> listarPorStatusAtivo();

    List<ClienteResponse> listarPorCep(String cepCodigo);

    List<ClienteResponse> listarPorNomeContendo(String nomeCliente);

    List<ClienteResponse> listarPorCidade(String cidadeNome);

    List<ClienteResponse> listarPorEstadoUf(String estadoUf);

    List<ClienteResponse> listarPorTelefoneNum(String numeroTelefone);

}
