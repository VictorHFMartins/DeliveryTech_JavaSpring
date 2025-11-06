package com.deliverytech.delivery.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.domain.model.Cidade;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Long> {

    List<Cidade> findByEstadoId(Long estadoId);

    List<Cidade> findByNomeContainingIgnoreCase(String nome);

    Optional<Cidade> findByNomeIgnoreCaseAndEstadoUfIgnoreCase(String nomeCidade, String ufNorm);

    boolean existeCidadeNaUf(String uf, String nomeCidade);

}
