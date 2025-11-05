package com.deliverytech.delivery.domain.model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(
        name = "restaurantes"
// opcional: indexes = { @Index(name = "uk_restaurante_cnpj", columnList = "cnpj", unique = true) }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "produtos")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Restaurante {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Column(nullable = false, length = 120)
    private String nome;

    @NotBlank(message = "CNPJ é obrigatório")
    // Regex simples para CNPJ com ou sem máscara; ajuste se quiser forçar só dígitos.
    @Pattern(
            regexp = "^(\\d{14}|\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2})$",
            message = "CNPJ inválido"
    )
    @Column(nullable = false, length = 18, unique = true)
    private String cnpj;

    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private List<Produto> produtos = new ArrayList<>();

    // Se for livre, mantenha String; se for fechado, troque por enum + @Enumerated
    @Size(max = 60)
    private String categoria;

    @Size(min = 8, max = 20, message = "Telefone deve ter entre 8 e 20 caracteres")
    @Pattern(regexp = "^[\\d\\-\\+\\(\\)\\s]{8,20}$", message = "Telefone inválido")
    private String telefone;

    @Email(message = "E-mail inválido")
    @Column(unique = true)
    private String email;

    private String endereco;

    // Se quiser precisão exata, troque para BigDecimal com scale 6
    @DecimalMin(value = "-90.0")
    @DecimalMax(value = "90.0")
    private double latitude;

    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    private double longitude;

    private LocalTime horarioAbertura;
    private LocalTime horarioFechamento;

    @AssertTrue(message = "Horário de abertura deve ser antes do fechamento")
    public boolean isHorarioValido() {
        return horarioAbertura == null || horarioFechamento == null
                || horarioAbertura.isBefore(horarioFechamento);
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Estado estado; // ABERTO, FECHADO, MANUTENCAO

    public enum Estado {
        ABERTO, FECHADO, MANUTENCAO
    }

    @Column(name = "data_cadastro", updatable = false)
    private LocalDateTime dataCadastro;

    @Column(nullable = false)
    private Boolean ativo;

    @PrePersist
    public void prePersist() {
        this.dataCadastro = LocalDateTime.now();
        if (this.ativo == null) {
            this.ativo = Boolean.TRUE;
        }
        if (this.estado == null) {
            this.estado = Estado.FECHADO;
        }
    }

    @Transient
    public String getCoordenada() {
        if (latitude == 0 && longitude == 0) {
            return "Coordenadas não definidas";
        }
        return latitude + ", " + longitude;
    }

    public void inativar() {
        this.ativo = false;
    }

    public void atualizarEstado(Estado novoEstado) {
        this.estado = novoEstado;
    }

    public boolean isAberto() {
        return this.estado == Estado.ABERTO;
    }

    public boolean isFechado() {
        return this.estado == Estado.FECHADO;
    }

    public boolean isEmManutencao() {
        return this.estado == Estado.MANUTENCAO;
    }

    public void atualizarCoordenadas(double lat, double lon) {
        this.latitude = lat;
        this.longitude = lon;
    }

    public void adicionarProduto(Produto produto) {
        if (produto == null) {
            return;
        }
        produto.setRestaurante(this);
        this.produtos.add(produto);
    }
}
