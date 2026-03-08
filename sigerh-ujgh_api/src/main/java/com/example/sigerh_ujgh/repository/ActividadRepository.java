package com.example.sigerh_ujgh.repository;

import com.example.sigerh_ujgh.entity.Tipo_actividades_administrativas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActividadRepository extends JpaRepository<Tipo_actividades_administrativas, Long> {

    List<Tipo_actividades_administrativas> findByActivoTrue();
}