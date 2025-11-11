package com.deliverytech.delivery.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.domain.model.Cep;

@Repository
public interface CepRepository extends JpaRepository<Cep, Long> {

    // Buscar por Código
    Optional<Cep> findByCodigo(String codigo);

    // Buscar ceps por cidade
    List<Cep> findByCidadeNomeContainingIgnoreCase(String nomeCidade);
}
