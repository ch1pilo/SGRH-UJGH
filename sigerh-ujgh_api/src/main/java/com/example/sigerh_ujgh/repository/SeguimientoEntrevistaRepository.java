package com.example.sigerh_ujgh.repository;

import com.example.sigerh_ujgh.entity.Seguimiento_entrevista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeguimientoEntrevistaRepository extends JpaRepository<Seguimiento_entrevista, Long> {

    // Busca todos los seguimientos de una entrevista en específico
    @Query("SELECT s FROM Seguimiento_entrevista s WHERE s.id_entrevista.id = :idEntrevista")
    List<Seguimiento_entrevista> findByIdEntrevista(@Param("idEntrevista") Long idEntrevista);

    // Busca los seguimientos activos/pendientes de una entrevista
    @Query("SELECT s FROM Seguimiento_entrevista s WHERE s.id_entrevista.id = :idEntrevista AND s.estado = 'PENDIENTE'")
    List<Seguimiento_entrevista> findPendientesByEntrevista(@Param("idEntrevista") Long idEntrevista);
}