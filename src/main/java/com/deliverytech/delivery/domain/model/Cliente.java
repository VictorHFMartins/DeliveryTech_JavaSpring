package com.deliverytech.delivery.domain.model;

import com.deliverytech.delivery.domain.enums.TipoTelefone;
import com.deliverytech.delivery.domain.enums.TipoUsuario;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor   
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Cliente extends Usuario {

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario;

    @PrePersist
    public void definirTipo() {
        if (this.tipoTelefone == null) {
            this.tipoTelefone = TipoTelefone.CLIENTE;
        }
        tipoUsuario = TipoUsuario.CLIENTE;
    }
}
