package com.gestao.funcionarios.dto;

public class DepartamentoResponseDTO {
    private Long id;
    private String nome;
    private String sigla;
    private Boolean ativo;

    public DepartamentoResponseDTO() {
    }

    public DepartamentoResponseDTO(Long id, String nome, String sigla, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.sigla = sigla;
        this.ativo = ativo;
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
}