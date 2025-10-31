package com.deliverytech.delivery.api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.deliverytech.delivery.domain.model.Produto;
import com.deliverytech.delivery.domain.model.Restaurante;
import com.deliverytech.delivery.service.RestauranteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/restaurantes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class RestauranteController {

    private final RestauranteService restauranteService;

    @GetMapping
    public ResponseEntity<List<Restaurante>> listar() {
        return ResponseEntity.ok(restauranteService.listarAtivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(restauranteService.buscarPorId(id));
    }

    @GetMapping("/nome")
    public ResponseEntity<List<Restaurante>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(restauranteService.buscarPorNome(nome));
    }

    @GetMapping("/email")
    public ResponseEntity<?> buscarPorEmail(@RequestParam String email) {
        return ResponseEntity.ok(restauranteService.buscarPorEmail(email));
    }

    @GetMapping("/abertos")
    public ResponseEntity<List<Restaurante>> listarAbertos() {
        return ResponseEntity.ok(restauranteService.listarAbertos());
    }

    @GetMapping("/por-estado")
    public ResponseEntity<List<Restaurante>> listarPorEstado(@RequestParam Restaurante.Estado estado) {
        return ResponseEntity.ok(restauranteService.listarPorEstado(estado));
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@Valid @RequestBody Restaurante restaurante) {
        Restaurante novoRestaurante = restauranteService.criar(restaurante);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novoRestaurante.getId())
                .toUri();

        return ResponseEntity.created(location).body(novoRestaurante);
    }

    @PostMapping("/{restauranteId}/produtos")
    public ResponseEntity<?> adicionarProduto(
            @PathVariable Long restauranteId,
            @Valid @RequestBody Produto produto) {

        Produto novoProduto = restauranteService.adicionarProduto(restauranteId, produto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{produtoId}")
                .buildAndExpand(novoProduto.getId())
                .toUri();

        return ResponseEntity.created(location).body(novoProduto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody Restaurante restaurante) {

        return ResponseEntity.ok(restauranteService.atualizar(id, restaurante));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> inativar(@PathVariable Long id) {
        restauranteService.inativar(id);
        return ResponseEntity.noContent().build();
    }

}
