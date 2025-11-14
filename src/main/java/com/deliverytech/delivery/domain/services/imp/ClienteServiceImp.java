package com.deliverytech.delivery.domain.services.imp;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.deliverytech.delivery.api.dto.ClienteRequest;
import com.deliverytech.delivery.api.dto.ClienteResponse;
import com.deliverytech.delivery.api.exceptions.BusinessException;
import com.deliverytech.delivery.api.exceptions.EntityNotFoundException;
import com.deliverytech.delivery.domain.model.Cliente;
import com.deliverytech.delivery.domain.model.Endereco;
import com.deliverytech.delivery.domain.model.Telefone;
import com.deliverytech.delivery.domain.repository.ClienteRepository;
import com.deliverytech.delivery.domain.repository.EnderecoRepository;
import com.deliverytech.delivery.domain.repository.TelefoneRepository;
import com.deliverytech.delivery.domain.services.ClienteService;
import com.deliverytech.delivery.domain.validator.UsuarioValidator;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ClienteServiceImp implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;

    private final UsuarioValidator usuarioValidator;

    private final ModelMapper modelMapper;

    @Override
    public ClienteResponse criar(ClienteRequest dto) {
        if (clienteRepository.existsByEmail(dto.email())) {
            throw new BusinessException("E-mail já cadastrado" + dto.email());
        }

        Cliente cliente = modelMapper.map(dto, Cliente.class);
        if (cliente == null) {
            throw new BusinessException("Erro ao mapear Cliente a partir do DTO");
        }

        Cliente clienteSalvo = clienteRepository.save(cliente);

        ClienteResponse response = modelMapper.map(clienteSalvo, ClienteResponse.class);
        if (response == null) {
            throw new BusinessException("Erro ao mapear Cliente para ClienteResponse");
        }

        return response;
    }

    @Override
    public ClienteResponse atualizar(Long id, ClienteRequest dto) {
        Cliente clienteExistente = (Cliente) usuarioValidator.validarUsuario(id);

        if (dto.email() == null || dto.email().isEmpty()) {
            throw new BusinessException("E-mail não pode ser vazio");
        }
        if (!clienteExistente.getEmail().equals(dto.email())
                && clienteRepository.existsByEmail(dto.email())) {
            throw new BusinessException("E-mail já cadastrado");
        }
        if (dto.nome() == null || dto.nome().isEmpty()) {
            throw new BusinessException("Nome não pode ser vazio");
        }
        if (dto.telefoneIds() == null) {
            throw new BusinessException("Deve existir ao menos um telefone");
        }
        if (dto.enderecoId() == null) {
            throw new BusinessException("EndereçoId não pode ser vazio");
        }

        clienteExistente.setNome(dto.nome());
        clienteExistente.setEmail(dto.email());

        if (!dto.telefoneIds().isEmpty()) {

            clienteExistente.getTelefones().clear();
            List<Telefone> novosTelefones = dto.telefoneIds().stream()
                    .filter(idTel -> idTel != null)
                    .map(idTel -> telefoneRepository.findById(Objects.requireNonNull(idTel))
                    .orElseThrow(() -> new EntityNotFoundException("Telefone não encontrado: " + idTel)))
                    .peek(t -> t.setUsuario(clienteExistente))
                    .collect(Collectors.toList());

            clienteExistente.getTelefones().addAll(novosTelefones);
        }
        Endereco endereco = validarEndereco(dto.enderecoId());
        clienteExistente.setEndereco(endereco);

        return ClienteResponse.of(clienteExistente);
    }

    public Endereco validarEndereco(Long enderecoId) {

        return enderecoRepository.findById(Objects.requireNonNull(enderecoId))
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado com ID: " + enderecoId));
    }

    @Override
    public ClienteResponse buscarPorId(long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Id não encontrado."));
        return ClienteResponse.of(cliente);
    }

    @Override
    public ClienteResponse buscarPorEmail(String email) {
        Cliente cliente = clienteRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("E-mail não encontrado: " + email));
        return ClienteResponse.of(cliente);
    }

    @Override
    public void ativarDesativar(Long clienteId) {
        Cliente cliente = (Cliente) usuarioValidator.validarUsuario(clienteId);

        cliente.setStatus(!cliente.isStatus());
    }

    @Override
    public List<ClienteResponse> listarPorNomeContendo(String nome) {
        List<Cliente> clientes = clienteRepository.findByNomeContainingIgnoreCase(nome);

        return clientes.stream()
                .map(ClienteResponse::of)
                .toList();
    }

    @Override
    public List<ClienteResponse> listarPorStatusAtivo() {
        List<Cliente> clientes = clienteRepository.findByStatusTrue();

        return clientes.stream()
                .map(ClienteResponse::of)
                .toList();
    }

    @Override
    public List<ClienteResponse> listarPorCep(String cepCodigo) {
        List<Cliente> clientes = clienteRepository.findByEnderecoCepCodigo(cepCodigo);

        return clientes.stream()
                .map(ClienteResponse::of)
                .toList();
    }

    @Override
    public List<ClienteResponse> listarPorCidade(String cidadeNome) {
        List<Cliente> clientes = clienteRepository.findByEnderecoCepCidadeNomeContainingIgnoreCase(cidadeNome);

        return clientes.stream()
                .map(ClienteResponse::of)
                .toList();
    }

    @Override
    public List<ClienteResponse> listarPorEstadoUf(String estadoUf) {
        List<Cliente> clientes = clienteRepository.findByEnderecoCepCidadeEstadoUfContainingIgnoreCase(estadoUf);

        return clientes.stream()
                .map(ClienteResponse::of)
                .toList();
    }

    @Override
    public List<ClienteResponse> listarPorTelefoneNum(String telefoneNum) {
        List<Cliente> clientes = clienteRepository.findByTelefonesNumeroContaining(telefoneNum);

        return clientes.stream()
                .map(ClienteResponse::of)
                .toList();
    }

}
