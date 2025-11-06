package com.deliverytech.delivery.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.domain.model.Telefone;

@Repository
public interface TelefoneRepository extends JpaRepository<Telefone, Long> {

    List<Telefone> findByClienteId(Long clienteId);

    boolean existsByClienteIdAndDddAndNumero(Long clienteId, String ddd, String numero);

}
