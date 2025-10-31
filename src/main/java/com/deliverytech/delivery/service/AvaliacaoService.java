package com.deliverytech.delivery.service;

import java.util.DoubleSummaryStatistics;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery.domain.model.Avaliacao;
import com.deliverytech.delivery.domain.model.Cliente;
import com.deliverytech.delivery.domain.model.Restaurante;
import com.deliverytech.delivery.domain.repository.AvaliacaoRepository;
import com.deliverytech.delivery.domain.repository.AvaliacaoRepository.RestauranteMediaView;
import com.deliverytech.delivery.domain.repository.ClienteRepository;
import com.deliverytech.delivery.domain.repository.RestauranteRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final ClienteRepository clienteRepository;
    private final RestauranteRepository restauranteRepository;

    public Avaliacao criar(Avaliacao avaliacao) {
        Long clienteId = avaliacao.getCliente() != null ? avaliacao.getCliente().getId() : null;
        Long restauranteId = avaliacao.getRestaurante() != null ? avaliacao.getRestaurante().getId() : null;

        Cliente cliente = findClienteOrThrow(clienteId);
        Restaurante restaurante = findRestauranteOrThrow(restauranteId);

        avaliacao.setCliente(cliente);
        avaliacao.setRestaurante(restaurante);

        return avaliacaoRepository.save(avaliacao);
    }

    public Avaliacao atualizar(Long id, Avaliacao dados) {
        Avaliacao existente = buscarPorId(id);

        if (dados.getNota() != null) {
            existente.setNota(dados.getNota());
        }
        if (dados.getComentario() != null) {
            existente.setComentario(dados.getComentario());
        }

        return avaliacaoRepository.save(existente);
    }

    public void deletar(Long id) {
        Avaliacao existente = buscarPorId(id);
        avaliacaoRepository.delete(existente);
    }

    @Transactional(readOnly = true)
    public Avaliacao buscarPorId(Long id) {
        return avaliacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Avaliação não encontrada: id=" + id));
    }

    @Transactional(readOnly = true)
    public List<Avaliacao> buscarPorRestaurante(Long restauranteId) {
        findRestauranteOrThrow(restauranteId);
        return avaliacaoRepository.findByRestauranteId(restauranteId);
    }

    @Transactional(readOnly = true)
    public List<Avaliacao> buscarPorCliente(Long clienteId) {
        findClienteOrThrow(clienteId);
        return avaliacaoRepository.findByClienteId(clienteId);
    }

    @Transactional(readOnly = true)
    public List<Avaliacao> buscarPorNota(Avaliacao.Nota nota) {
        return avaliacaoRepository.findByNota(nota);
    }

    @Transactional(readOnly = true)
    public List<RestauranteRankingDTO> listarRestaurantesPorNotaAsc() {
        List<RestauranteMediaView> rows = avaliacaoRepository.listarRestaurantesOrdenadosPorMediaAsc();
        return rows.stream()
                .map(r -> new RestauranteRankingDTO(
                r.getRestaurante().getId(),
                r.getRestaurante().getNome(),
                r.getMedia()
        ))
                .toList();
    }

    @Transactional(readOnly = true)
    public double calcularMediaDoRestaurante(Long restauranteId) {
        findRestauranteOrThrow(restauranteId);
        List<Avaliacao> avaliacoes = avaliacaoRepository.findByRestauranteId(restauranteId);
        if (avaliacoes.isEmpty()) {
            return 0.0;
        }

        DoubleSummaryStatistics stats = avaliacoes.stream()
                .mapToDouble(a -> a.getNota().ordinal())
                .summaryStatistics();

        return stats.getAverage();
    }

    private Cliente findClienteOrThrow(Long id) {
        if (id == null) {
            throw new EntityNotFoundException("Cliente não informado");
        }
        return clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado: id=" + id));
    }

    private Restaurante findRestauranteOrThrow(Long id) {
        if (id == null) {
            throw new EntityNotFoundException("Restaurante não informado");
        }
        return restauranteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado: id=" + id));
    }

    public record RestauranteRankingDTO(Long restauranteId, String nome, Double media) {

    }
}
