package com.deliverytech.delivery.domain.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.domain.enums.CategoriaProduto;
import com.deliverytech.delivery.domain.model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // Buscar todos os produtos ativos e inativos
    List<Produto> findByAtivoTrue();

    List<Produto> findByAtivoFalse();

    // Buscar produtos por nome (contendo, ignorando maiúsculas/minúsculas)
    List<Produto> findByNomeContainingIgnoreCase(String nome);

    // Buscar produtos por restaurante
    List<Produto> findByRestauranteId(Long restauranteId);

    List<Produto> findByRestauranteIdOrderByNomeAsc(Long restauranteId);

    List<Produto> findByRestauranteIdAndAtivoTrue(Long restauranteId);

    // Buscar por categoria
    List<Produto> findByCategoria(CategoriaProduto categoria);

    // Verificar duplicidades
    boolean existsByRestauranteIdAndNomeIgnoreCase(Long restauranteId, String nome);

    boolean existsByRestauranteIdAndNomeIgnoreCaseAndIdNot(Long restauranteId, String nome, Long id);

    //filtro entre valores
    List<Produto> findByPrecoBetween(BigDecimal precoMin, BigDecimal precoMax);
    // List<Produto> findByPrecoLessThanEqual(BigDecimal preco);

}
