package com.deliverytech.delivery.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery.domain.model.Cliente;
import com.deliverytech.delivery.domain.model.Pedido;
import com.deliverytech.delivery.domain.model.Pedido.Status;
import com.deliverytech.delivery.domain.model.Restaurante;
import com.deliverytech.delivery.domain.repository.ClienteRepository;
import com.deliverytech.delivery.domain.repository.PedidoRepository;
import com.deliverytech.delivery.domain.repository.RestauranteRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final RestauranteRepository restauranteRepository;

    public record CreatePedidoRequest(
            @NotNull Long clienteId,
            @NotNull Long restauranteId,
            String observacoes,
            BigDecimal valorTotal
            ) {

    }

    public record PedidoResponse(
            Long id,
            String numeroPedido,
            String status,
            BigDecimal valorTotal,
            String clienteNome,
            String restauranteNome
            ) {

    }

    public Pedido criarEntity(CreatePedidoRequest req) {
        Cliente cliente = clienteRepository.findById(req.clienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado: " + req.clienteId()));
        Restaurante restaurante = restauranteRepository.findById(req.restauranteId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado: " + req.restauranteId()));

        Pedido pedido = Pedido.builder()
                .numeroPedido(novoNumero())
                .status(Pedido.Status.PENDENTE)
                .valorTotal(req.valorTotal() != null ? req.valorTotal() : BigDecimal.ZERO)
                .observacoes(req.observacoes())
                .cliente(cliente)
                .restaurante(restaurante)
                .build();

        return pedidoRepository.save(pedido);
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarEntities() {
        return pedidoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Pedido buscarEntityPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado: " + id));
    }

    public PedidoResponse criar(CreatePedidoRequest req) {
        return toResponse(criarEntity(req));
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> listar() {
        return pedidoRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public PedidoResponse buscarPorId(Long id) {
        return toResponse(buscarEntityPorId(id));
    }

    private String novoNumero() {
        return "PED-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private PedidoResponse toResponse(Pedido p) {
        return new PedidoResponse(
                p.getId(),
                p.getNumeroPedido(),
                p.getStatus().name(),
                p.getValorTotal(),
                p.getCliente().getNome(),
                p.getRestaurante().getNome()
        );
    }

    @Transactional
    public Pedido atualizarStatus(Long id, Status novoStatus) {
        Pedido pedido = buscarEntityPorId(id);

        Status atual = pedido.getStatus();
        if (atual == novoStatus) {
            return pedido;
        }

        if (!transicaoValida(atual, novoStatus)) {
            throw new IllegalStateException("Transição de status inválida: " + atual + " -> " + novoStatus);
        }

        pedido.setStatus(novoStatus);
        return pedidoRepository.save(pedido);
    }

    private boolean transicaoValida(Status de, Status para) {
        return switch (de) {
            case PENDENTE ->
                para == Status.CONFIRMADO || para == Status.CANCELADO;
            case CONFIRMADO ->
                para == Status.ENTREGUE || para == Status.CANCELADO;
            case ENTREGUE, CANCELADO ->
                false;
            default ->
                false;
        };
    }

}
