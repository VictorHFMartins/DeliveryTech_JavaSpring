package com.deliverytech.delivery.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.domain.model.Cidade;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Long> {

    // Busca cidades por nome
    List<Cidade> findByNomeContainingIgnoreCase(String nome);

    // Buscar cidades por UF do estado
    List<Cidade> findByEstadoUfIgnoreCase(String uf);

    // Buscar Cidades por estado 
    Optional<Cidade> findByNomeIgnoreCaseAndEstadoUfIgnoreCase(String nome, String estadoUf);

    // busca cidades pelo cep
    List<Cidade> findByCepsCodigoContainingIgnoreCase(String cepCodigo);
}
