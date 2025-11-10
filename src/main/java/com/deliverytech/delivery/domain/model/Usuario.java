package com.deliverytech.delivery.domain.model;

import java.time.LocalDateTime;
import java.util.List;

import com.deliverytech.delivery.domain.enums.TipoTelefone;

import jakarta.persistence.*;
import lombok.*;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    protected Long id;

    @Column(nullable = false, length = 100)
    protected String nome;

    @Column(nullable = false, unique = true)
    protected String email;

    @Enumerated(EnumType.STRING)
    protected TipoTelefone tipoTelefone;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    protected List<Telefone> telefones;

    @ManyToOne
    @JoinColumn(name = "endereco_id", nullable = false)
    protected Endereco endereco;

    @Column(nullable = false)
    protected boolean status;

    @Column(name = "data_cadastro", nullable = false)
    protected LocalDateTime dataCadastro;

    @PrePersist
    protected void onCreate() {
        if (dataCadastro == null) {
            dataCadastro = LocalDateTime.now();
        }
        status = true;
    }
}
