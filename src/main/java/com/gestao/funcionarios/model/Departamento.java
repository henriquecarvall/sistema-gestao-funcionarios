package com.gestao.funcionarios.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departamentos")
public class Departamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Column(unique = true, nullable = false)
    private String nome;

    @NotBlank(message = "Sigla é obrigatória")
    @Column(nullable = false)
    private String sigla;

    @Column(nullable = false)
    private Boolean ativo = true;

    @OneToMany(mappedBy = "departamento", cascade = CascadeType.ALL)
    private List<Funcionario> funcionarios = new ArrayList<>();

    public Departamento() {
    }

    public Departamento(String nome, String sigla) {
        this.nome = nome;
        this.sigla = sigla;
        this.ativo = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public List<Funcionario> getFuncionarios() {
        return funcionarios;
    }

    public void setFuncionarios(List<Funcionario> funcionarios) {
        this.funcionarios = funcionarios;
    }

    @Override
    public String toString() {
        return "Departamento{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", sigla='" + sigla + '\'' +
                ", ativo=" + ativo +
                '}';
    }
}