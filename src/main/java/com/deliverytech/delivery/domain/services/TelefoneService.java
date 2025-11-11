package com.deliverytech.delivery.domain.services;

import java.util.List;

import com.deliverytech.delivery.api.dto.TelefoneResponse;
import com.deliverytech.delivery.domain.enums.TipoUsuario;

public interface TelefoneService {

    TelefoneResponse criar(TelefoneService telefone);

    TelefoneResponse atualizar(long id, TelefoneService telefone);

    TelefoneResponse buscarTelefonePorId(long id);

    List<TelefoneResponse> listarNumerosAtivos();

    List<TelefoneResponse> listarPorDdd(String ddd);

    List<TelefoneResponse> listarPorUsuárioId(Long id);

    List<TelefoneResponse> listarTelefonePorNumeroContendo(String Numero);

    List<TelefoneResponse> listarNumerosDeClientes(TipoUsuario tipoUsuario);

}
