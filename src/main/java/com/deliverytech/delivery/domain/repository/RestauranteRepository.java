package com.deliverytech.delivery.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.domain.enums.EstadoRestaurante;
import com.deliverytech.delivery.domain.model.Restaurante;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    // Buscar Restaurante por categoria (método derivado)
    Optional<Restaurante> findByClasse(String email);

    // Buscar Restaurante ativos
    List<Restaurante> findByAtivoTrue();


    /*implementar taxa de entregas */
    // List<taxa> findByTaxaEntregaLessThanEqual(BigDecimal taxa);

    /*implementar top 5 */
    // List<Restaurante> findTop5ByOrderByNomeAsc();
    // Buscar Restaurante por email (método derivado)
    Optional<Restaurante> findByEmail(String email);

    // Verificar se email já existe
    boolean existsByEmail(String email);

    // Buscar Restaurante por nome (contendo)
    List<Restaurante> findByNomeContainingIgnoreCase(String nome);

    // Busca Restaurante por estado
    List<Restaurante> findByEstado(EstadoRestaurante estado);

    Optional<Restaurante> findByCnpj(String cnpj);

    boolean existsByCnpj(String cnpj);

}
