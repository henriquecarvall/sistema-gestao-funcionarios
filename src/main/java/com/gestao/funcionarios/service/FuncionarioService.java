package com.gestao.funcionarios.service;

import com.gestao.funcionarios.dto.FuncionarioRequestDTO;
import com.gestao.funcionarios.dto.FuncionarioResponseDTO;
import com.gestao.funcionarios.model.Funcionario;
import com.gestao.funcionarios.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository repository;

    public List<FuncionarioResponseDTO> findAll() {
        return repository.findAllByOrderByNomeAsc()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public Optional<FuncionarioResponseDTO> findById(Long id) {
        return repository.findById(id).map(this::toResponseDTO);
    }

    public List<FuncionarioResponseDTO> findByCargo(String cargo) {
        return repository.findByCargo(cargo)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<FuncionarioResponseDTO> findByAtivo(Boolean ativo) {
        return repository.findByAtivoOrderByNomeAsc(ativo)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional
    public FuncionarioResponseDTO create(FuncionarioRequestDTO request) {
        if (repository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }

        Funcionario funcionario = new Funcionario();
        funcionario.setNome(request.getNome().trim());
        funcionario.setEmail(request.getEmail().toLowerCase());
        funcionario.setCargo(request.getCargo().trim());
        funcionario.setSalario(request.getSalario());
        funcionario.setDataAdmissao(request.getDataAdmissao());
        funcionario.setAtivo(true);

        Funcionario saved = repository.save(funcionario);
        return toResponseDTO(saved);
    }

    @Transactional
    public FuncionarioResponseDTO update(Long id, FuncionarioRequestDTO request) {
        Funcionario funcionario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));

        if (!funcionario.getAtivo()) {
            throw new RuntimeException("Funcionário inativo não pode ser editado");
        }

        if (repository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new RuntimeException("Email já cadastrado em outro funcionário");
        }

        if (request.getSalario().compareTo(funcionario.getSalario()) < 0) {
            throw new RuntimeException("Salário não pode ser reduzido");
        }

        funcionario.setNome(request.getNome().trim());
        funcionario.setEmail(request.getEmail().toLowerCase());
        funcionario.setCargo(request.getCargo().trim());
        funcionario.setSalario(request.getSalario());
        funcionario.setDataAdmissao(request.getDataAdmissao());

        Funcionario updated = repository.save(funcionario);
        return toResponseDTO(updated);
    }

    @Transactional
    public void inactivate(Long id) {
        Funcionario funcionario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));

        funcionario.setAtivo(false);
        repository.save(funcionario);
    }

    private FuncionarioResponseDTO toResponseDTO(Funcionario funcionario) {
        return new FuncionarioResponseDTO(
                funcionario.getId(),
                funcionario.getNome(),
                funcionario.getEmail(),
                funcionario.getCargo(),
                funcionario.getSalario(),
                funcionario.getDataAdmissao(),
                funcionario.getAtivo()
        );
    }
}