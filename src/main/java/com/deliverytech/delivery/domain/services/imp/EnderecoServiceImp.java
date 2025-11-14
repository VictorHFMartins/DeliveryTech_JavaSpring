package com.deliverytech.delivery.domain.services.imp;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.deliverytech.delivery.api.dto.CepRequest;
import com.deliverytech.delivery.api.dto.EnderecoRequest;
import com.deliverytech.delivery.api.dto.EnderecoResponse;
import com.deliverytech.delivery.api.dto.EnderecoUpdateRequest;
import com.deliverytech.delivery.api.exceptions.BusinessException;
import com.deliverytech.delivery.domain.enums.TipoLogradouro;
import com.deliverytech.delivery.domain.model.Cep;
import com.deliverytech.delivery.domain.model.Cliente;
import com.deliverytech.delivery.domain.model.Endereco;
import com.deliverytech.delivery.domain.model.Restaurante;
import com.deliverytech.delivery.domain.model.Usuario;
import com.deliverytech.delivery.domain.repository.ClienteRepository;
import com.deliverytech.delivery.domain.repository.EnderecoRepository;
import com.deliverytech.delivery.domain.repository.RestauranteRepository;
import com.deliverytech.delivery.domain.services.CepService;
import com.deliverytech.delivery.domain.services.EnderecoService;
import com.deliverytech.delivery.domain.validator.UsuarioValidator;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class EnderecoServiceImp implements EnderecoService {

    private final ClienteRepository clienteRepository;
    private final EnderecoRepository enderecoRepository;
    private final RestauranteRepository restauranteRepository;

    private final CepService cepService;

    private final UsuarioValidator usuarioValidator;

    private final ModelMapper modelMapper;

    @Override
    public Endereco buscarOuCriarEndereco(EnderecoRequest dto) {
        String numero = dto.numero();
        String logradouroNome = dto.logradouro();

        Optional<Endereco> seExiste = enderecoRepository.findByNumeroAndLogradouroIgnoreCase(numero, logradouroNome);

        if (seExiste.isPresent()) {
            return seExiste.get();
        }

        Endereco endereco = modelMapper.map(dto, Endereco.class);
        if (endereco == null) {
            throw new BusinessException("Erro ao mapear endereco a partir do DTO");
        }

        CepRequest cepDto = new CepRequest(dto.cepCodigo(), dto.cidadeId());

        Cep cep = cepService.buscarOuCriar(cepDto);
        endereco.setCep(cep);

        return enderecoRepository.save(endereco);
    }

    @Override
    public EnderecoResponse adicionar(Long usuarioId, EnderecoRequest dto) {
        Usuario usuario = usuarioValidator.validarUsuario(usuarioId);

        Endereco endereco = buscarOuCriarEndereco(dto);

        usuario.setEndereco(endereco);
        if (usuario instanceof Cliente cliente) {
            clienteRepository.save(cliente);
        }
        if (usuario instanceof Restaurante restaurante) {
            restauranteRepository.save(restaurante);
        }

        return EnderecoResponse.of(endereco);
    }

    @Override
    public EnderecoResponse atualizar(Long usuarioId, Long enderecoId, EnderecoUpdateRequest dto) {
        Usuario usuario = usuarioValidator.validarUsuario(usuarioId);

        Endereco endereco = enderecoRepository.findById(Objects.requireNonNull(enderecoId))
                .orElseThrow(() -> new EntityNotFoundException("Endereco não encontrado para o id: " + enderecoId));

        endereco.setLogradouro(dto.logradouro());
        endereco.setTipoLogradouro(dto.tipoLogradouro());
        endereco.setNumero(dto.numero());
        endereco.setComplemento(dto.complemento());
        endereco.setBairro(dto.bairro());

        CepRequest cepDto = new CepRequest(dto.cepCodigo(), dto.cidadeId());
        Cep cep = cepService.buscarOuCriar(cepDto);
        endereco.setCep(cep);

        endereco.setLatitude(dto.latitude());
        endereco.setLongitude(dto.longitude());

        enderecoRepository.save(endereco);
        usuario.setEndereco(endereco);

        if (usuario instanceof Cliente cliente) {
            clienteRepository.save(cliente);
        }
        if (usuario instanceof Restaurante restaurante) {
            restauranteRepository.save(restaurante);
        }
        return EnderecoResponse.of(endereco);
    }

    @Override
    public EnderecoResponse buscarPorId(Long idEndereco) {
        return EnderecoResponse.of(enderecoRepository.findById(Objects.requireNonNull(idEndereco))
                .orElseThrow(() -> new EntityNotFoundException("Endereco não encontrado para o id: " + idEndereco)));
    }

    @Override
    public EnderecoResponse buscarPorNumeroELogradouro(String numero, String logradouroNome) {
        return EnderecoResponse.of(enderecoRepository.findByNumeroAndLogradouroIgnoreCase(numero, logradouroNome)
                .orElseThrow(() -> new EntityNotFoundException("Endereco não encontrado para os parametros: " + numero + " e " + logradouroNome)));
    }

    @Override
    public List<EnderecoResponse> listarPorBairro(String bairroNome) {
        List<Endereco> enderecos = enderecoRepository.findByBairroContainingIgnoreCase(bairroNome);
        return enderecos.stream()
                .map(EnderecoResponse::of)
                .toList();
    }

    @Override
    public List<EnderecoResponse> listarPorCepCodigo(String CepCodigo) {
        List<Endereco> enderecos = enderecoRepository.findByCepCodigoContainingIgnoreCase(CepCodigo);
        return enderecos.stream()
                .map(EnderecoResponse::of)
                .toList();
    }

    @Override
    public List<EnderecoResponse> listarPorLogradouro(String LogradouroNome) {
        List<Endereco> enderecos = enderecoRepository.findByLogradouroContainingIgnoreCase(LogradouroNome);
        return enderecos.stream()
                .map(EnderecoResponse::of)
                .toList();
    }

    @Override
    public List<EnderecoResponse> listarPorTipoLogradouro(TipoLogradouro tipoLogradouro) {
        List<Endereco> enderecos = enderecoRepository.findByTipoLogradouro(tipoLogradouro);
        return enderecos.stream()
                .map(EnderecoResponse::of)
                .toList();
    }
}
