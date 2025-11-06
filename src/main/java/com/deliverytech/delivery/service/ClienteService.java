package com.deliverytech.delivery.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery.domain.model.Cliente;
import com.deliverytech.delivery.domain.model.Telefone;
import com.deliverytech.delivery.domain.repository.CepRepository;
import com.deliverytech.delivery.domain.repository.ClienteRepository;
import com.deliverytech.delivery.domain.repository.EnderecoRepository;
import com.deliverytech.delivery.domain.repository.TelefoneRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final EnderecoRepository enderecoRepository;
    private final CepRepository cepRepository;
    private final TelefoneRepository telefoneRepository;

    // Cadastrar novo cliente
    public Cliente criar(Cliente cliente) {
        validarDadosCliente(cliente);

        // normalizar email 
        cliente.setEmail(normalizarEmail(cliente.getEmail()));

        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado: " + cliente.getEmail());
        }

        // Garantir que o Endereco esteja vinculado a um CEP válido
        if (cliente.getEndereco() == null || cliente.getEndereco().getCep() == null) {
            throw new IllegalArgumentException("O endereço e o CEP são obrigatórios.");
        }

        var cep = cepRepository.findByCodigo(cliente.getEndereco().getCep().getCodigo())
                .orElseThrow(() -> new EntityNotFoundException("CEP não encontrado."));

        cliente.getEndereco().setCep(cep);
        enderecoRepository.save(cliente.getEndereco());

        // Associar cliente em cada telefone antes de salvar
        if (cliente.getTelefones() != null) {
            for (Telefone t : cliente.getTelefones()) {
                boolean existe = telefoneRepository
                        .existsByClienteIdAndDddAndNumero(cliente.getId(), t.getDdd(), t.getNumero());
                if (existe) {
                    throw new IllegalArgumentException("Telefone já cadastrado para este cliente: ("
                            + t.getDdd() + ") " + t.getNumero());
                }
                t.setCliente(cliente);
            }
        }

        // ativo por padrão
        cliente.setAtivo(true);
        return clienteRepository.save(cliente);
    }

    // Buscar cliente por ID
    @Transactional(readOnly = true)
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado: " + id));
    }

    // Buscar cliente por email
    @Transactional(readOnly = true)
    public Cliente buscarPorEmail(String email) {
        String emailNorm = normalizarEmail(email);
        return clienteRepository.findByEmail(emailNorm)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com email: " + emailNorm));
    }

    // Listar todos os clientes ativos
    @Transactional(readOnly = true)
    public List<Cliente> listarAtivos() {
        return clienteRepository.findByAtivoTrue();
    }

    // Atualizar dados do cliente
    public Cliente atualizar(Long id, Cliente clienteAtualizado) {
        Cliente existente = buscarPorId(id);

        validarDadosCliente(clienteAtualizado);

        String novoEmail = normalizarEmail(clienteAtualizado.getEmail());

        // se email mudou, verificar duplicidade
        if (!existente.getEmail().equals(novoEmail) && clienteRepository.existsByEmail(novoEmail)) {
            throw new IllegalArgumentException("Email já cadastrado: " + novoEmail);
        }

        // Atualizar campos permitidos
        existente.setNome(clienteAtualizado.getNome());
        existente.setEmail(novoEmail);

        // Atualiza o endereço (mantendo CEP original)
        if (clienteAtualizado.getEndereco() != null) {
            existente.getEndereco().setLogradouro(clienteAtualizado.getEndereco().getLogradouro());
            existente.getEndereco().setNumero(clienteAtualizado.getEndereco().getNumero());
            existente.getEndereco().setComplemento(clienteAtualizado.getEndereco().getComplemento());
        }

        // Substitui lista de telefones
        existente.getTelefones().clear();
        if (clienteAtualizado.getTelefones() != null) {
            for (Telefone t : clienteAtualizado.getTelefones()) {
                boolean existe = telefoneRepository
                        .existsByClienteIdAndDddAndNumero(clienteAtualizado.getId(), t.getDdd(), t.getNumero());
                if (existe) {
                    throw new IllegalArgumentException("Telefone já cadastrado para este cliente: ("
                            + t.getDdd() + ") " + t.getNumero());
                }
                t.setCliente(clienteAtualizado);
            }
        }

        return clienteRepository.save(existente);
    }

    public void ativar(Long id) {
        Cliente cliente = buscarPorId(id);
        if (cliente.getAtivo()) {
            throw new IllegalArgumentException("Cliente já está ativo: " + id);
        }
        cliente.ativar();
        clienteRepository.save(cliente);

    }

    // Inativar cliente (soft delete)
    public void inativar(Long id) {
        Cliente cliente = buscarPorId(id);

        if (!cliente.getAtivo()) {
            throw new IllegalArgumentException("Cliente já está inativo: " + id);
        }

        cliente.inativar();
        clienteRepository.save(cliente);
    }

    // Buscar clientes por nome
    @Transactional(readOnly = true)
    public List<Cliente> buscarPorNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome);
    }

    // ===== Validações / Util =====
    private void validarDadosCliente(Cliente cliente) {
        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        if (cliente.getNome().trim().length() < 2) {
            throw new IllegalArgumentException("Nome deve ter pelo menos 2 caracteres");
        }
        if (cliente.getEmail() == null || cliente.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }
    }

    private String normalizarEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }
}
