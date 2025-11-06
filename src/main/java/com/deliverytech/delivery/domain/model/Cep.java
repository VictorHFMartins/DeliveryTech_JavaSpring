package com.deliverytech.delivery.domain.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "cep")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"cidade", "enderecos"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Cep {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true, length = 9)
    private String codigo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cidade_id", nullable = false)
    private Cidade cidade;

    @OneToMany(mappedBy = "cep", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Endereco> enderecos;

}
