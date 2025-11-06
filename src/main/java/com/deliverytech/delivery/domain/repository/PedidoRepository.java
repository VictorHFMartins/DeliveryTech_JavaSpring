package com.deliverytech.delivery.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.domain.enums.StatusPedido;
import com.deliverytech.delivery.domain.model.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByClienteId(Long clienteId);

    List<Pedido> findByStatus(StatusPedido status);

    // List<Pedido> findTop10ByOrderByDataPedidoDesc();
    List<Pedido> findByDataPedidoBetween(LocalDateTime inicio, LocalDateTime fim);

    List<Pedido> findByRestauranteId(Long restauranteId);

    boolean existsByNumeroPedido(String numeroPedido);

}
