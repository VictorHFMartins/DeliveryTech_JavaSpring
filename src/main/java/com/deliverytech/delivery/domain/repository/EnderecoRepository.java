package com.deliverytech.delivery.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.domain.enums.TipoLogradouro;
import com.deliverytech.delivery.domain.model.Endereco;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

    // Busca residência por id de usuário
    Endereco findByUsuariosId(Long UsuarioId);

    // Buscar pelo numero da residência e nome ddo logradouro
    Optional<Endereco> findByNumeroAndLogradouroIgnoreCase(String numero, String LogradouroNome);

    // verifica pelo numero da residência e nome ddo logradouro
    boolean existsByNumeroAndLogradouroIgnoreCase(String numero, String LogradouroNome);

    // Lista residências por bairro
    List<Endereco> findByBairroContainingIgnoreCase(String bairro);

    // Lista residências por tipo de logradouro
    List<Endereco> findByTipoLogradouro(TipoLogradouro tipoLogradouro);

    // Buscar por nome do logradouro
    List<Endereco> findByLogradouroContainingIgnoreCase(String logradouroNome);

    // Buscar por código do CEP
    List<Endereco> findByCepsCodigoContainingIgnoreCase(String codigoPostal);
}
