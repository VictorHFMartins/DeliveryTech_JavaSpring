package com.deliverytech.delivery.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.domain.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Buscar cliente por email (método derivado)
    Optional<Cliente> findByEmail(String email);

    // Verificar se email já existe
    boolean existsByEmail(String email);

    // Buscar clientes ativos
    List<Cliente> findByAtivoTrue();

    // Buscar clientes por nome (parcial ou não)
    List<Cliente> findByNomeContainingIgnoreCase(String nome);

    // Busca clientes por cep
    List<Cliente> findByEnderecoCepCodigo(String cepCodigo);

    // Busca Clientes por cidade
    List<Cliente> findByEnderecoCepCidadeNomeContainingIgnoreCase(String cidadeNome);

    // Busca Clientes por estado
    List<Cliente> findByEnderecoCepCidadeEstadoUfContainingIgnoreCase(String estadoUf);

    // busca clientes por numero;
    List<Cliente> findByTelefoneNumeroContaining(String numero);

}
