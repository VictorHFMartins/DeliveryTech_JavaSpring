package com.deliverytech.delivery.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery.domain.enums.CategoriaProduto;
import com.deliverytech.delivery.domain.model.Produto;
import com.deliverytech.delivery.domain.model.Restaurante;
import com.deliverytech.delivery.domain.repository.ProdutoRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public Produto criar(Produto produto) {
        validarDadosProduto(produto);

        if (produto.getRestaurante() == null || produto.getRestaurante().getId() == null) {
            throw new IllegalArgumentException("Produto deve estar associado a um restaurante válido.");
        }

        produto.setAtivo(true);
        return produtoRepository.save(produto);
    }

    @Transactional(readOnly = true)
    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado: " + id));
    }

    @Transactional(readOnly = true)
    public List<Produto> listarAtivos() {
        return produtoRepository.findByAtivoTrue();
    }

    public Produto atualizar(Long id, Produto produtoAtualizado) {
        Produto produto = buscarPorId(id);
        validarDadosProduto(produtoAtualizado);

        Restaurante novoRestaurante = produtoAtualizado.getRestaurante();

        if (!produto.getRestaurante().equals(novoRestaurante)
                && produtoRepository.existsByRestauranteIdAndNomeIgnoreCase(
                        novoRestaurante.getId(), produtoAtualizado.getNome())) {
            throw new IllegalArgumentException("Já existe um produto com esse nome nesse restaurante.");
        }

        produto.setNome(produtoAtualizado.getNome());
        produto.setDescricao(produtoAtualizado.getDescricao());
        produto.setPreco(produtoAtualizado.getPreco());
        produto.setCategoria(produtoAtualizado.getCategoria());

        return produtoRepository.save(produto);
    }

    public void inativar(Long id) {
        Produto produto = buscarPorId(id);
        if (!produto.getAtivo()) {
            throw new IllegalArgumentException("Produto já está inativo: " + id);
        }
        produto.inativar();
        produtoRepository.save(produto);
    }

    public void ativar(Long id) {
        Produto produto = buscarPorId(id);
        if (produto.getAtivo()) {
            throw new IllegalArgumentException("Produto já está ativo: " + id);
        }
        produto.ativar();
        produtoRepository.save(produto);
    }

    @Transactional(readOnly = true)
    public List<Produto> buscarPorNome(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome);
    }

    @Transactional(readOnly = true)
    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Produto> listarInativos() {
        return produtoRepository.findByAtivoFalse();
    }

    @Transactional(readOnly = true)
    public List<Produto> listarPorRestaurante(Long id) {
        return produtoRepository.findByRestauranteId(id);
    }

    @Transactional(readOnly = true)
    public List<Produto> buscarPorCategoria(CategoriaProduto categoria) {
        return produtoRepository.findByCategoria(categoria);
    }

    @Transactional(readOnly = true)
    public List<Produto> buscarPorFaixaPreco(BigDecimal precoMin, BigDecimal precoMax) {
        return produtoRepository.findByPrecoBetween(precoMin, precoMax);
    }

    public Produto atualizarEstoque(Long id, int quantidade) {
        Produto produto = buscarPorId(id);
        produto.setEstoque(Math.max(0, quantidade));
        return produtoRepository.save(produto);
    }

    private void validarDadosProduto(Produto produto) {
        if (produto.getPreco() == null || produto.getPreco().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Preço deve ser um valor positivo");
        }
        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
    }
}
