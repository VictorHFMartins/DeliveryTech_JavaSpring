package com.deliverytech.delivery.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.domain.model.Restaurante;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    // Buscar Restaurante por email (método derivado)
    Optional<Restaurante> findByEmail(String email);

    // Verificar se email já existe
    boolean existsByEmail(String email);

    // Buscar Restaurante ativos
    List<Restaurante> findByAtivoTrue();

    // Buscar Restaurante por nome (contendo)
    List<Restaurante> findByNomeContainingIgnoreCase(String nome);

    // Busca Restaurante por estado
    List<Restaurante> findByEstado(Restaurante.Estado estado);

    Optional<Restaurante> findByCnpj(String cnpj);

    boolean existsByCnpj(String cnpj);

}
