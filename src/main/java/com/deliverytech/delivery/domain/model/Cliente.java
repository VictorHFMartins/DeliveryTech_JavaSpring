package com.deliverytech.delivery.domain.model;

import com.deliverytech.delivery.domain.enums.TipoUsuario;

import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Cliente extends Usuario {

    @PrePersist
    public void definirTipo() {
        if (tipoUsuario != null) {
            tipoUsuario = TipoUsuario.CLIENTE;
        }
    }
}
