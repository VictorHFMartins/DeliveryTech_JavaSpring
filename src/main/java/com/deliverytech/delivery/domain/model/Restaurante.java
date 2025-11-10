package com.deliverytech.delivery.domain.model;

import java.time.LocalTime;

import com.deliverytech.delivery.domain.enums.ClasseRestaurante;
import com.deliverytech.delivery.domain.enums.EstadoRestaurante;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import lombok.*;

@Entity
@Table(name = "restaurantes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Restaurante extends Usuario {

    @Column(length = 18, nullable = false)
    private String cnpj;

    @Column(nullable = false)
    private ClasseRestaurante classe;

    @Column(nullable = true)
    private LocalTime horarioAbertura;

    @Column(nullable = true)
    private LocalTime horarioFechamento;

    @AssertTrue(message = "Horário de abertura deve ser antes do fechamento")
    public boolean isHorarioValido() {
        return horarioAbertura == null || horarioFechamento == null
                || horarioAbertura.isBefore(horarioFechamento);
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoRestaurante estado;

    @PrePersist
    public void definirEstado() {
        if (this.estado == null) {
            this.estado = EstadoRestaurante.ABERTO;
        }
    }

}
