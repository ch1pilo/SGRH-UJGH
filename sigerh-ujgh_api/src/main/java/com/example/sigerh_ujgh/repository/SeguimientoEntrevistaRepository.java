package com.example.sigerh_ujgh.repository;

import com.example.sigerh_ujgh.entity.Seguimiento_entrevista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeguimientoEntrevistaRepository extends JpaRepository<Seguimiento_entrevista, Long> {

    // Trae el historial de una sola entrevista, ordenado por ID descendente (lo más reciente primero)
    @Query("SELECT s FROM Seguimiento_entrevista s WHERE s.id_entrevista.id = :idEntrevista ORDER BY s.id DESC")
    List<Seguimiento_entrevista> buscarHistorialPorEntrevista(@Param("idEntrevista") Long idEntrevista);
}