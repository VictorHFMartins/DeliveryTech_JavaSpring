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

import com.deliverytech.delivery.domain.enums.NotaAvaliacao;
import com.deliverytech.delivery.domain.model.Avaliacao;
import com.deliverytech.delivery.service.AvaliacaoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/avaliacoes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    /* ======= CRUD ======= */
    @PostMapping
    public ResponseEntity<Avaliacao> criar(@Valid @RequestBody Avaliacao avaliacao) {
        Avaliacao nova = avaliacaoService.criar(avaliacao);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nova.getId())
                .toUri();
        return ResponseEntity.created(location).body(nova);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Avaliacao> atualizar(@PathVariable Long id, @RequestBody Avaliacao novaAvaliacao) {
        return ResponseEntity.ok(avaliacaoService.atualizar(id, novaAvaliacao));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        avaliacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Avaliacao> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(avaliacaoService.buscarPorId(id));
    }

    /* ======= CONSULTAS ======= */
    @GetMapping("/restaurante/{id}")
    public ResponseEntity<List<Avaliacao>> buscarPorRestaurante(@PathVariable Long id) {
        return ResponseEntity.ok(avaliacaoService.buscarPorRestaurante(id));
    }

    @GetMapping("/cliente/{id}")
    public ResponseEntity<List<Avaliacao>> buscarPorCliente(@PathVariable Long id) {
        return ResponseEntity.ok(avaliacaoService.buscarPorCliente(id));
    }

    @GetMapping("/nota")
    public ResponseEntity<List<Avaliacao>> buscarPorNota(
            @RequestParam("nota") NotaAvaliacao nota) {
        if (nota == null) {
            return ResponseEntity.ok(List.of());
        }
        return ResponseEntity.ok(avaliacaoService.buscarPorNota(nota));
    }

    @GetMapping("/restaurantes/ordenados-por-nota")
    public ResponseEntity<List<AvaliacaoService.RestauranteRankingDTO>> listarRestaurantesPorNotaAsc() {
        return ResponseEntity.ok(avaliacaoService.listarRestaurantesPorNotaAsc());
    }
}
