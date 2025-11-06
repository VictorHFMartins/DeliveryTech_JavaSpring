package com.deliverytech.delivery.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.domain.enums.NotaAvaliacao;
import com.deliverytech.delivery.domain.model.Avaliacao;

@Repository

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    // Buscar todas as avaliações de um restaurante
    List<Avaliacao> findByRestauranteId(Long restauranteId);

    // Buscar todas as avaliações feitas por um cliente
    List<Avaliacao> findByClienteId(Long clienteId);

    // Buscar avaliações por nota
    List<Avaliacao> findByNota(NotaAvaliacao nota);

    // 🔹 Listar restaurantes ordenados pela média das notas (ASC)
    @Query("""
       SELECT a.restaurante AS restaurante,
              AVG(
                 CASE a.nota
                   WHEN com.deliverytech.delivery.domain.model.Avaliacao$Nota.PESSIMO   THEN 0
                   WHEN com.deliverytech.delivery.domain.model.Avaliacao$Nota.RUIM      THEN 1
                   WHEN com.deliverytech.delivery.domain.model.Avaliacao$Nota.REGULAR   THEN 2
                   WHEN com.deliverytech.delivery.domain.model.Avaliacao$Nota.BOM       THEN 3
                   WHEN com.deliverytech.delivery.domain.model.Avaliacao$Nota.OTIMO     THEN 4
                   WHEN com.deliverytech.delivery.domain.model.Avaliacao$Nota.EXCELENTE THEN 5
                 END
              ) AS media
       FROM Avaliacao a
       GROUP BY a.restaurante
       ORDER BY media ASC
       """)

    List<RestauranteMediaView> listarRestaurantesOrdenadosPorMediaAsc();

    public interface RestauranteMediaView {

        com.deliverytech.delivery.domain.model.Restaurante getRestaurante();

        Double getMedia();
    }
}
