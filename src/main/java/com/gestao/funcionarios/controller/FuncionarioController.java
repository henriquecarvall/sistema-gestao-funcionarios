package com.gestao.funcionarios.controller;

import com.gestao.funcionarios.dto.FuncionarioRequestDTO;
import com.gestao.funcionarios.dto.FuncionarioResponseDTO;
import com.gestao.funcionarios.service.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/funcionarios")
public class FuncionarioController {

    @Autowired
    private FuncionarioService service;

    @GetMapping
    public ResponseEntity<List<FuncionarioResponseDTO>> findAll(
            @RequestParam(required = false) String cargo,
            @RequestParam(required = false) Boolean ativo) {

        if (cargo != null && ativo != null) {
            List<FuncionarioResponseDTO> result = service.findAll()
                    .stream()
                    .filter(f -> f.getCargo().equalsIgnoreCase(cargo) && f.getAtivo().equals(ativo))
                    .toList();
            return ResponseEntity.ok(result);
        }

        if (cargo != null) {
            return ResponseEntity.ok(service.findByCargo(cargo));
        }

        if (ativo != null) {
            return ResponseEntity.ok(service.findByAtivo(ativo));
        }

        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDTO> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody FuncionarioRequestDTO request) {
        try {
            FuncionarioResponseDTO created = service.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Email já cadastrado")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody FuncionarioRequestDTO request) {
        try {
            FuncionarioResponseDTO updated = service.update(id, request);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<?> inactivate(@PathVariable Long id) {
        try {
            service.inactivate(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}