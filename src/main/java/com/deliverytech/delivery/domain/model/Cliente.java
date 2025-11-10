package com.deliverytech.delivery.domain.model;

import com.deliverytech.delivery.domain.enums.TipoTelefone;

import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "clientes")
@Builder
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Cliente extends Usuario {

    @PrePersist
    public void definirTipo() {
        if (this.tipoTelefone == null) {
            this.tipoTelefone = TipoTelefone.CLIENTE;
        }
    }
}
