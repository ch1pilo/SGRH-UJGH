package com.example.sigerh_ujgh.repository;

import com.example.sigerh_ujgh.entity.Carga_administrativa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CargaAdministrativaRepository extends JpaRepository<Carga_administrativa, Long> {

    @Query("SELECT c FROM Carga_administrativa c WHERE c.id_carga_academica.id = :idCarga")
    List<Carga_administrativa> buscarPorCargaId(@Param("idCarga") Long idCarga);
}