# Delivery Tech API

Sistema de delivery desenvolvido em **Spring Boot 3.5.7 e **Java 21**.

## 🚀 Tecnologias & Stack

* Java 21 (LTS)
* Spring Boot 3.5.7

  * Spring Web
  * Spring Data JPA
  * Validation (Jakarta Validation)
* Banco: **H2** (em memória)
* Lombok
* DevTools
* Maven

### ⚡ Recursos do Java utilizados

* Records
* Text Blocks
* Pattern Matching
* (Pronto para) Virtual Threads

---

## 🏗️ Domínio (Entidades principais)

### Cliente

* Campos: `id`, `nome`, `email (único)`, `telefone`, `endereco`, `dataCadastro`, `ativo`
* Validações: `@NotBlank(nome)`, `@Email(email)`, `@Pattern` e `@Size` (telefone)
* `@PrePersist`: seta `dataCadastro` e `ativo = true` se nulo
* Operação: `inativar()`

### Restaurante

* Campos: `id`, `nome`, `cnpj (único, regex)`, `categoria`, `telefone`, `email (único)`, `endereco`,
  `latitude`, `longitude`, `horarioAbertura/Fechamento`, `estado (ABERTO|FECHADO|MANUTENCAO)`,
  `dataCadastro`, `ativo`
* **Importante**: removida a coleção `produtos` para evitar `LazyInitializationException` na serialização
* Operações auxiliares: `inativar()`, `atualizarEstado()`, `isAberto()` etc.

### Produto

* Campos: `id`, `nome`, `descricao`, `categoria (BEBIDAS|COMIDAS|SOBREMESAS)`, `estoque`,
  `preco (BigDecimal)`, `ativo`, `dataCadastro`, `restaurante (ManyToOne)`
* `@PrePersist`: seta `dataCadastro`, normaliza `ativo` e zera estoque negativo
* Operações: `ativar()`, `inativar()`

### Avaliação

* Campos: `id`, `cliente (ManyToOne)`, `restaurante (ManyToOne)`,
  `nota (enum: PESSIMO|RUIM|REGULAR|BOM|OTIMO|EXCELENTE)`, `comentario`, `dataAvaliacao`
* `@PrePersist`: seta `dataAvaliacao`
* Repositório inclui **ranking por média de notas** (projeção `RestauranteMediaView`)

### Pedido

* Campos: `id`, `numeroPedido`, `status (enum: PENDENTE|CONFIRMADO|ENTREGUE|CANCELADO)`,
  `valorTotal`, `observacoes`, `cliente (ManyToOne)`, `restaurante (ManyToOne)`
* **DTOs internos do `PedidoService`**:

  * `CreatePedidoRequest(clienteId, restauranteId, observacoes, valorTotal)`
  * `PedidoResponse(id, numeroPedido, status, valorTotal, clienteNome, restauranteNome)`
* **Regras de transição de status**:

  * `PENDENTE -> CONFIRMADO | CANCELADO`
  * `CONFIRMADO -> ENTREGUE | CANCELADO`
  * `ENTREGUE` e `CANCELADO` são estados finais

---

## 🌐 Endpoints

### Health

* `GET /health` → status UP + timestamp + service
* `GET /health/info` → metadados (app, versão, dev, RA, curso, campus, Java, Spring)

### Clientes (`/clientes`)

* `GET /clientes` → **lista clientes ativos**
* `GET /clientes/{id}` → busca por id
* `GET /clientes/buscar?nome=` → busca contendo (case-insensitive)
* `GET /clientes/email?value=` → busca por e-mail
* `POST /clientes` → cadastra
* `PUT /clientes/{id}` → atualiza
* `PUT /cliente/ativar/{id}` → ativa cliente inativo
* `DELETE /clientes/{id}` → **inativa** (soft delete)


### Restaurantes (`/restaurantes`)

* `GET /restaurantes` → **lista restaurantes ativos**
* `GET /restaurantes/{id}` → busca por id
* `GET /restaurantes/buscar?nome=` → busca contendo (case-insensitive)
* `GET /restaurantes/email?value=` → busca por e-mail
* `GET /restaurantes/estado?value=ABERTO|FECHADO|MANUTENCAO` → busca por estado do restaurante ABERTO|FECHADO|MANUTENCAO
* `POST /restaurantes` → cadastra
* `POST /restaurantes/{restauranteId}/produtos` → adiciona produto a um restaurante
* `PUT /restaurantes/{id}` → atualiza
* `DELETE /restaurantes/{id}` → **inativa**

### Produtos (`/produtos`)

* `GET /produtos` → lista todos
* `GET /produtos/{id}` → busca por id
* `GET /produtos/nome?value=` → busca contendo (case-insensitive)
* `GET /produtos/categoria?categoria=BEBIDAS|COMIDAS|SOBREMESAS`
* `GET /produtos/preco?min=&max=` → faixa de preço
* `GET /produtos/ativos` → somente ativos
* `GET /produtos/inativos` → somente inativos
* `GET /produtos/restaurante/{id}` → produtos de um restaurante
* `PUT /produtos/{id}/estoque?quantidade=` → atualiza estoque
* `PUT /produtos/{id}` → atualiza
* `POST /produtos` → cadastra
* `DELETE /produtos/{id}` → **inativa**

### Avaliações (`/avaliacoes`)

* `GET /avaliacoes/{id}` → busca por id
* `GET /avaliacoes/restaurante/{id}` ou `GET /avaliacoes/restaurante?id=`
* `GET /avaliacoes/cliente/{id}` ou `GET /avaliacoes/cliente?id=`
* `GET /avaliacoes/nota?nota=PESSIMO|RUIM|REGULAR|BOM|OTIMO|EXCELENTE`
* `GET /avaliacoes/restaurantes/ordenados-por-nota` → Busca ordenada em ordem ascendente
* `POST /avaliacoes` → cadastra
* `PUT /avaliacoes/{id}` → atualiza nota/comentário
* `DELETE /avaliacoes/{id}` → remove

> **Atenção:** verifique o `@RequestMapping` do controller de avaliações.
> Use `/avaliacoes` (sem o typo `/avaliaccoes`).

### Pedidos (`/pedidos`)

* `GET /pedidos` → lista (DTO `PedidoResponse`)
* `GET /pedidos/{id}` → busca (DTO `PedidoResponse`)
* `POST /pedidos` → cria (DTO `CreatePedidoRequest`)
* `PUT /pedidos/{id}/status?status=PENDENTE|CONFIRMADO|ENTREGUE|CANCELADO` → transição com validação
* `DELETE /pedidos/{id}` → **cancela** (atalho para setar `CANCELADO`)

---

## 📦 Exemplos (payloads)

### Criar Cliente

```json
POST /clientes
{
  "nome": "João Silva",
  "email": "joao@email.com",
  "telefone": "(11) 99999-1111",
  "endereco": "Rua A, 123"
}
```

### Criar Restaurante

```json
POST /restaurantes
{
  "nome": "Pizzaria Bella",
  "cnpj": "12.345.678/0001-90",
  "categoria": "Italiana",
  "telefone": "(11) 3333-1111",
  "email": "contato@pizzariabella.com",
  "endereco": "Av. Paulista, 1000"
}
```

### Adicionar Produto a um Restaurante

```json
POST /restaurantes/1/produtos
{
  "nome": "Pizza Margherita",
  "descricao": "Molho de tomate, mussarela e manjericão",
  "categoria": "COMIDAS",
  "estoque": 10,
  "preco": 35.90
}
```

### Criar Pedido

```json
POST /pedidos
{
  "clienteId": 1,
  "restauranteId": 1,
  "observacoes": "Sem cebola",
  "valorTotal": 54.80
}
```

### Atualizar Status do Pedido

```
PUT /pedidos/1/status?status=CONFIRMADO
```

---

## 🔧 Configuração & Execução

1. **Pré-requisitos**: JDK 21
2. **Clonar o repositório**
   `git clone https://github.com/VictorHFMartins/DeliveryTech_JavaSpring.git`
3. **Executar**
   `mvn spring-boot:run`
4. **Acessos**

   * Health: `http://localhost:8080/health`
   * Health (info): `http://localhost:8080/health/info`
   * H2 Console: `http://localhost:8080/h2-console`

### H2 (padrão)

* URL JDBC: `jdbc:h2:mem:testdb`
* Usuário/Senha: (padrão do Spring/H2 — ajuste em `application.properties` se necessário)

---

## 📝 Observações importantes

* **Enums como `STRING`**: certifique-se de mapear `@Enumerated(EnumType.STRING)` (ex.: `Pedido.status`) e alinhar qualquer `schema.sql`/`data.sql` para evitar erros de conversão (ex.: H2 tentando ler `PENDENTE` como numérico).
* **Lazy vs JSON**: para evitar `LazyInitializationException` na serialização, **não** exponha coleções `@OneToMany` diretamente no JSON (ex.: removemos `List<Produto>` de `Restaurante`). Considere DTOs ou `@JsonIgnore`.
* **Validações**: as entidades usam `Jakarta Validation`. Campos obrigatórios e formatos (e-mail, telefone, CNPJ) já estão cobertos.

---

## ✅ Roadmap curto (próximos passos)

* Finalizar listagem de produtos **por restaurante** (garantir query correta no service/repository).
* Adicionar endpoint opcional: listar restaurantes por `estado`.
* Implementar paginação e ordenação nos endpoints mais volumosos.
* Adicionar **ControllerAdvice** para padronizar erros (quando você quiser).

---

## 👨‍💻 Autor

**Victor Hugo Faria Martins**
Universidade Anhembi Morumbi
Extensão: Arquitetura de Sistemas API REST Full com Spring Boot

> Desenvolvido com **Java 21** e **Spring Boot 3.5.x** — uso acadêmico/educacional.
