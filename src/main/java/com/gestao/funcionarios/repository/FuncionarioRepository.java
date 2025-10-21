package com.gestao.funcionarios.repository;

import com.gestao.funcionarios.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    // Busca funcionário por email (para verificar duplicidade)
    Optional<Funcionario> findByEmail(String email);

    // Busca funcionário por email ignorando caso (case insensitive)
    Optional<Funcionario> findByEmailIgnoreCase(String email);

    // Busca funcionários por cargo
    List<Funcionario> findByCargo(String cargo);

    // Busca funcionários por status (ativo/inativo)
    List<Funcionario> findByAtivo(Boolean ativo);

    // Busca funcionários por cargo e status
    List<Funcionario> findByCargoAndAtivo(String cargo, Boolean ativo);

    // Busca todos os funcionários ordenados por nome
    List<Funcionario> findAllByOrderByNomeAsc();

    // Busca funcionários ativos ordenados por nome
    List<Funcionario> findByAtivoOrderByNomeAsc(Boolean ativo);

    // Busca se existe funcionário com email (para validação)
    boolean existsByEmail(String email);

    // Busca se existe funcionário com email ignorando o ID atual (para edição)
    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM Funcionario f WHERE f.email = :email AND f.id != :id")
    boolean existsByEmailAndIdNot(@Param("email") String email, @Param("id") Long id);

    // Busca funcionário ativo por ID
    Optional<Funcionario> findByIdAndAtivoTrue(Long id);
}