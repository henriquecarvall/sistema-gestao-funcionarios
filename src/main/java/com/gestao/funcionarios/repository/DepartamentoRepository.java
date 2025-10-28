package com.gestao.funcionarios.repository;

import com.gestao.funcionarios.model.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {

    Optional<Departamento> findByNome(String nome);

    boolean existsByNome(String nome);

    @Query("SELECT d FROM Departamento d WHERE d.ativo = true")
    List<Departamento> findByAtivoTrue();

    boolean existsByNomeAndIdNot(String nome, Long id);
}