package com.deliverytech.delivery.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.domain.model.Estado;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long> {

    List<Estado> findByNomeContainingIgnoreCase(String nome);

    Optional<Estado> findByUfIgnoreCase(String ufNorm);

}
