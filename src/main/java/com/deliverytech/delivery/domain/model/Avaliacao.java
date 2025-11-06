package com.deliverytech.delivery.domain.model;

import java.time.LocalDateTime;

import com.deliverytech.delivery.domain.enums.NotaAvaliacao;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "avaliacoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Cliente que fez a avaliação
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    // Restaurante avaliado
    @ManyToOne
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;

    // Nota atribuída
    @Enumerated(EnumType.STRING)
    private NotaAvaliacao nota;

    private String comentario;

    private LocalDateTime dataAvaliacao;

    @PrePersist
    public void prePersist() {
        this.dataAvaliacao = LocalDateTime.now();
    }
}
