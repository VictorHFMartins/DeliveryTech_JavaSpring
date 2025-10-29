package com.deliverytech.delivery.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity @Table(name = "clientes")
@Data @NoArgsConstructor @AllArgsConstructor
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    @NotBlank(message = "O nome é obrigatório") 
    private String nome;

    @Email(message = "E-mail inválido")
    private String email;
    
    @Size(min = 8, max = 15, message = "Telefone deve ter entre 8 e 15 caracteres")
    private String telefone;

    private String endereco;

    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    @Column(nullable = false)
    private Boolean ativo;

    @PrePersist
    public void prePersist() {
        this.dataCadastro = LocalDateTime.now();
        if (this.ativo == null)
            this.ativo = true;
    }
    public void inativar() {
        this.ativo = false;
    }

}
