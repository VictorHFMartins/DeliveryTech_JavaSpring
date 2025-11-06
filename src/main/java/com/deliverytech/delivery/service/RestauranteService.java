package com.deliverytech.delivery.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery.domain.enums.EstadoRestaurante;
import com.deliverytech.delivery.domain.model.Produto;
import com.deliverytech.delivery.domain.model.Restaurante;
import com.deliverytech.delivery.domain.repository.ProdutoRepository;
import com.deliverytech.delivery.domain.repository.RestauranteRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RestauranteService {

    private final RestauranteRepository restauranteRepository;
    private final ProdutoRepository produtoRepository;

    /* ===== CRUD ===== */
    public Restaurante criar(Restaurante restaurante) {
        validarDadosRestaurante(restaurante);

        String email = normalizarEmail(restaurante.getEmail());
        restaurante.setEmail(email);

        if (email != null && restauranteRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email já cadastrado: " + email);
        }

        // ativo por padrão (a entidade também seta em @PrePersist)
        restaurante.setAtivo(true);
        return restauranteRepository.save(restaurante);
    }

    @Transactional(readOnly = true)
    public Restaurante buscarPorId(Long id) {
        return restauranteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado: " + id));
    }

    @Transactional(readOnly = true)
    public Restaurante buscarPorEmail(String email) {
        String emailNorm = normalizarEmail(email);
        return restauranteRepository.findByEmail(emailNorm)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado com email: " + emailNorm));
    }

    @Transactional(readOnly = true)
    public List<Restaurante> listarAtivos() {
        return restauranteRepository.findByAtivoTrue();
    }

    @Transactional(readOnly = true)
    public List<Restaurante> listarAbertos() {
        return restauranteRepository.findByEstado(EstadoRestaurante.ABERTO);
    }

    public Restaurante atualizar(Long id, Restaurante dados) {
        Restaurante r = buscarPorId(id);
        validarDadosRestaurante(dados);

        // Email
        String novoEmail = normalizarEmail(dados.getEmail());
        if (novoEmail != null && !novoEmail.equals(r.getEmail()) && restauranteRepository.existsByEmail(novoEmail)) {
            throw new IllegalArgumentException("Email já cadastrado: " + novoEmail);
        }

        r.setNome(dados.getNome());
        r.setEmail(novoEmail);
        r.setTelefone(dados.getTelefone());
        r.setEndereco(dados.getEndereco());
        r.setClasse(dados.getClasse());

        r.setHorarioAbertura(dados.getHorarioAbertura());
        r.setHorarioFechamento(dados.getHorarioFechamento());
        r.setEstado(dados.getEstado());

        if (dados.getLatitude() != 0 || dados.getLongitude() != 0) {
            r.atualizarCoordenadas(dados.getLatitude(), dados.getLongitude());
        }

        return restauranteRepository.save(r);
    }

    public void inativar(Long id) {
        Restaurante r = buscarPorId(id);
        if (Boolean.FALSE.equals(r.getAtivo())) {
            throw new IllegalArgumentException("Restaurante já está inativo: " + id);
        }
        r.inativar();
        restauranteRepository.save(r);
    }

    @Transactional(readOnly = true)
    public List<Restaurante> buscarPorNome(String nome) {
        return restauranteRepository.findByNomeContainingIgnoreCase(nome);
    }

    private void validarDadosRestaurante(Restaurante r) {
        if (r.getNome() == null || r.getNome().trim().length() < 2) {
            throw new IllegalArgumentException("Nome é obrigatório e deve ter pelo menos 2 caracteres");
        }
        if (r.getEmail() == null || r.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }
        if (r.getCnpj() == null || r.getCnpj().trim().isEmpty()) {
            throw new IllegalArgumentException("CNPJ é obrigatório");
        }
    }

    private String normalizarEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }

    public Produto adicionarProduto(Long restauranteId, Produto produto) {
        Restaurante restaurante = buscarPorId(restauranteId);
        produto.setId(null);

        produto.setRestaurante(restaurante);

        Produto salvo = produtoRepository.save(produto);

        return salvo;
    }

    public List<Restaurante> listarPorEstado(EstadoRestaurante estado) {
        if (estado == null) {
            throw new IllegalArgumentException("O estado do restaurante não pode ser nulo.");
        }
        return restauranteRepository.findByEstado(estado);
    }
}
