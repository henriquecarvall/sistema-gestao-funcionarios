package com.gestao.funcionarios.service;

import com.gestao.funcionarios.dto.FuncionarioRequestDTO;
import com.gestao.funcionarios.dto.FuncionarioResponseDTO;
import com.gestao.funcionarios.dto.DepartamentoResponseDTO;
import com.gestao.funcionarios.model.Departamento;
import com.gestao.funcionarios.model.Funcionario;
import com.gestao.funcionarios.repository.DepartamentoRepository;
import com.gestao.funcionarios.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    public List<FuncionarioResponseDTO> findAll() {
        return funcionarioRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<FuncionarioResponseDTO> findByAtivo(Boolean ativo) {
        return funcionarioRepository.findByAtivo(ativo).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<FuncionarioResponseDTO> findByCargo(String cargo) {
        return funcionarioRepository.findByCargo(cargo).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<FuncionarioResponseDTO> findById(Long id) {
        return funcionarioRepository.findById(id)
                .map(this::toResponseDTO);
    }

    public FuncionarioResponseDTO create(FuncionarioRequestDTO requestDTO) {
        if (requestDTO.getDepartamentoId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Departamento é obrigatório");
        }

        Departamento departamento = departamentoRepository.findById(requestDTO.getDepartamentoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Departamento não encontrado"));

        if (!departamento.getAtivo()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível vincular funcionário a departamento inativo");
        }

        if (funcionarioRepository.existsByEmail(requestDTO.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um funcionário com este email");
        }

        Funcionario funcionario = new Funcionario();
        funcionario.setNome(requestDTO.getNome());
        funcionario.setEmail(requestDTO.getEmail());
        funcionario.setCargo(requestDTO.getCargo());
        funcionario.setSalario(requestDTO.getSalario());
        funcionario.setDataAdmissao(requestDTO.getDataAdmissao());
        funcionario.setAtivo(true);
        funcionario.setDepartamento(departamento);

        Funcionario saved = funcionarioRepository.save(funcionario);
        return toResponseDTO(saved);
    }

    public FuncionarioResponseDTO update(Long id, FuncionarioRequestDTO requestDTO) {
        Funcionario funcionarioExistente = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário não encontrado"));

        if (requestDTO.getDepartamentoId() != null) {
            Departamento departamento = departamentoRepository.findById(requestDTO.getDepartamentoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Departamento não encontrado"));

            if (!departamento.getAtivo()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível vincular funcionário a departamento inativo");
            }
            funcionarioExistente.setDepartamento(departamento);
        }

        if (funcionarioRepository.existsByEmailAndIdNot(requestDTO.getEmail(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe outro funcionário com este email");
        }

        funcionarioExistente.setNome(requestDTO.getNome());
        funcionarioExistente.setEmail(requestDTO.getEmail());
        funcionarioExistente.setCargo(requestDTO.getCargo());
        funcionarioExistente.setSalario(requestDTO.getSalario());
        funcionarioExistente.setDataAdmissao(requestDTO.getDataAdmissao());

        Funcionario updated = funcionarioRepository.save(funcionarioExistente);
        return toResponseDTO(updated);
    }

    public void inactivate(Long id) {
        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário não encontrado"));

        funcionario.setAtivo(false);
        funcionarioRepository.save(funcionario);
    }

    public void delete(Long id) {
        if (!funcionarioRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário não encontrado");
        }
        funcionarioRepository.deleteById(id);
    }

    private FuncionarioResponseDTO toResponseDTO(Funcionario funcionario) {
        DepartamentoResponseDTO departamentoDTO = null;
        if (funcionario.getDepartamento() != null) {
            departamentoDTO = new DepartamentoResponseDTO(
                    funcionario.getDepartamento().getId(),
                    funcionario.getDepartamento().getNome(),
                    funcionario.getDepartamento().getSigla(),
                    funcionario.getDepartamento().getAtivo()
            );
        }

        return new FuncionarioResponseDTO(
                funcionario.getId(),
                funcionario.getNome(),
                funcionario.getEmail(),
                funcionario.getCargo(),
                funcionario.getSalario(),
                funcionario.getDataAdmissao(),
                funcionario.getAtivo(),
                departamentoDTO
        );
    }
}