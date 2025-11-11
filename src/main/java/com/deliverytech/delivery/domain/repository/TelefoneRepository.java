package com.deliverytech.delivery.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.domain.enums.TipoUsuario;
import com.deliverytech.delivery.domain.model.Telefone;

@Repository
public interface TelefoneRepository extends JpaRepository<Telefone, Long> {

    // Busca lista de telefones de usuário
    List<Telefone> findByUsuarioId(long id);

    // Lista telefones por tipo de usuário
    List<Telefone> findByTipoUsuario(TipoUsuario tipoUsuario);

    // Lista Telefones por ddd
    List<Telefone> findByDdd(String ddd);

    // Busca telefone por número
    List<Telefone> findByNumero(String numero);

    // Busca por numeros ativos
    List<Telefone> findByAtivoTrue();
    
}
