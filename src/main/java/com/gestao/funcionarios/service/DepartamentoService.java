package com.gestao.funcionarios.service;

import com.gestao.funcionarios.model.Departamento;
import com.gestao.funcionarios.repository.DepartamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@Service
public class DepartamentoService {

    @Autowired
    private DepartamentoRepository departamentoRepository;

    public List<Departamento> findAll() {
        return departamentoRepository.findAll();
    }

    public List<Departamento> findAtivos() {
        return departamentoRepository.findByAtivoTrue();
    }

    public Optional<Departamento> findById(Long id) {
        return departamentoRepository.findById(id);
    }

    public Departamento save(Departamento departamento) {
        if (departamentoRepository.existsByNome(departamento.getNome())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um departamento com este nome");
        }
        return departamentoRepository.save(departamento);
    }

    public Departamento update(Long id, Departamento departamentoAtualizado) {
        Departamento departamentoExistente = departamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Departamento não encontrado"));

        if (departamentoRepository.existsByNomeAndIdNot(departamentoAtualizado.getNome(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe outro departamento com este nome");
        }

        departamentoExistente.setNome(departamentoAtualizado.getNome());
        departamentoExistente.setSigla(departamentoAtualizado.getSigla());

        return departamentoRepository.save(departamentoExistente);
    }

    public void inativar(Long id) {
        Departamento departamento = departamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Departamento não encontrado"));

        departamento.setAtivo(false);
        departamentoRepository.save(departamento);
    }

    public void delete(Long id) {
        if (!departamentoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Departamento não encontrado");
        }
        departamentoRepository.deleteById(id);
    }
}