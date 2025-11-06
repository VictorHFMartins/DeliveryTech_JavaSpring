package com.deliverytech.delivery.domain.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.domain.model.Endereco;

@Repository
public interface EnderecoRepository extends org.springframework.data.jpa.repository.JpaRepository<Endereco, Long> {

    List<Endereco> findByCepCodigo(String cep);

    List<Endereco> findByCep_Id(Long cepId);

    List<Endereco> findByBairroContainingIgnoreCase(String bairro);

    List<Endereco> findByCepCidadeNomeContainingIgnoreCase(String cidade);

}
