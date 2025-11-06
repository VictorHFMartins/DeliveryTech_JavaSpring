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
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "cidades", uniqueConstraints = @UniqueConstraint(columnNames = {"nome", "estado_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"estado", "ceps"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Cidade {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, name = "nome", length = 25)
    private String nome;

    @ManyToOne(optional = false)
    @JoinColumn(name = "estado_id", nullable = false)
    private Estado estado;

    @OneToMany(mappedBy = "cidade", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Cep> ceps;
}
