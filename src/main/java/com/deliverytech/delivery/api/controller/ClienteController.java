package com.deliverytech.delivery.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.deliverytech.delivery.domain.model.Cliente;
import com.deliverytech.delivery.service.ClienteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ClienteController {

    final private ClienteService clienteService;

    /**
     * Cadastrar novo cliente
     */
    @PostMapping
    public ResponseEntity<?> cadastrar(@Valid @RequestBody Cliente cliente) {
        Cliente novoCliente = clienteService.novoCliente(cliente);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novoCliente.getId())
                .toUri();
        return ResponseEntity.created(location).body(novoCliente);
    }

    /**
     * Listar todos os clientes ativos
     */
    @GetMapping
    public ResponseEntity<List<Cliente>> listar() {
        return ResponseEntity.ok(clienteService.listarAtivos());
    }

    /**
     * Buscar cliente por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    /**
     * Atualizar cliente
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody Cliente cliente) {
        return ResponseEntity.ok(clienteService.atualizar(id, cliente));
    }

    /**
     * Inativar cliente (soft delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> inativar(@PathVariable Long id) {
            clienteService.inativar(id);
            return ResponseEntity.noContent().build();
    }

    /**
     * Buscar clientes por nome
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Cliente>> buscarPorNome(@RequestParam String nome) {
        List<Cliente> clientes = clienteService.buscarPorNome(nome);
        return ResponseEntity.ok(clientes);
    }

    /**
     * Buscar cliente por email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<?> buscarPorEmail(@PathVariable String email) {
        Cliente cliente = clienteService.buscarPorEmail(email);
        return ResponseEntity.ok(cliente);
    }
}
