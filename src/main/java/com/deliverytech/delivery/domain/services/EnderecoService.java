package com.deliverytech.delivery.domain.services;

import java.util.List;

import com.deliverytech.delivery.api.dto.EnderecoRequest;
import com.deliverytech.delivery.api.dto.EnderecoResponse;
import com.deliverytech.delivery.api.dto.EnderecoUpdateRequest;
import com.deliverytech.delivery.domain.enums.TipoLogradouro;
import com.deliverytech.delivery.domain.model.Endereco;

public interface EnderecoService {

    Endereco buscarOuCriarEndereco(EnderecoRequest enderecoDto);

    EnderecoResponse adicionar(Long usuarioId, EnderecoRequest enderecoDto);

    EnderecoResponse atualizar(Long usuarioId, Long enderecoId, EnderecoUpdateRequest enderecoUpdateDto);

    EnderecoResponse buscarPorId(Long idEndereco);

    List<EnderecoResponse> listarPorBairro(String bairroNome);

    List<EnderecoResponse> listarPorCepCodigo(String cepCodigo);

    List<EnderecoResponse> listarPorLogradouro(String logradouroNome);

    List<EnderecoResponse> listarPorTipoLogradouro(TipoLogradouro tipoLogradouro);

    EnderecoResponse buscarPorNumeroELogradouro(String numeroEndereco, String logradouroNome);

}
