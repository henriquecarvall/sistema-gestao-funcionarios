package com.gestao.funcionarios.controller;

import com.gestao.funcionarios.model.Departamento;
import com.gestao.funcionarios.service.DepartamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/departamentos")
public class DepartamentoController {

    @Autowired
    private DepartamentoService departamentoService;

    @GetMapping
    public List<Departamento> getAll() {
        return departamentoService.findAll();
    }

    @GetMapping("/ativos")
    public List<Departamento> getAtivos() {
        return departamentoService.findAtivos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Departamento> getById(@PathVariable Long id) {
        Optional<Departamento> departamento = departamentoService.findById(id);
        return departamento.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Departamento> create(@RequestBody Departamento departamento) {
        try {
            Departamento novoDepartamento = departamentoService.save(departamento);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoDepartamento);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Departamento> update(@PathVariable Long id, @RequestBody Departamento departamento) {
        try {
            Departamento departamentoAtualizado = departamentoService.update(id, departamento);
            return ResponseEntity.ok(departamentoAtualizado);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        try {
            departamentoService.inativar(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            departamentoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}