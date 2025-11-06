package com.deliverytech.delivery.domain.model;

import java.util.List;

import com.deliverytech.delivery.domain.enums.TipoLogradouro;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "enderecos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "cep")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Endereco {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "nome é obrigatório")
    @Column(nullable = false, length = 150)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoLogradouro logradouro;

    @NotBlank(message = "numero é obrigatório")
    @Column(nullable = false, length = 10)
    private String numero;

    @Column(length = 50)
    private String complemento;

    @NotBlank(message = "bairro é obrigatório")
    @Column(nullable = false, length = 100)
    private String bairro;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cep_id", nullable = false)
    private Cep cep;

    @OneToMany(mappedBy = "endereco", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Cliente> cliente;

    @PrePersist
    public void PrePersist() {
        if (complemento == null || complemento.isEmpty() || complemento.isBlank()) {
            complemento = "Sem complemento";
        }
    }
}
