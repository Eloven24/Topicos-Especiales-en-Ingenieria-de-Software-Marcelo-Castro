package com.eafit.nutrition.repository;

import com.eafit.nutrition.model.Nutricionista;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NutricionistaRepository extends JpaRepository<Nutricionista, Long> {

    @Query("SELECT n FROM Nutricionista n LEFT JOIN FETCH n.pacientes WHERE n.id = :id")
    Optional<Nutricionista> findByIdWithPacientes(@Param("id") Long id);

    @EntityGraph(attributePaths = {"pacientes"})
    @Query("SELECT n FROM Nutricionista n WHERE n.id = :id")
    Optional<Nutricionista> findByIdWithPacientesGraph(@Param("id") Long id);
}