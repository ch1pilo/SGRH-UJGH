package com.example.sigerh_ujgh.repository;
import com.example.sigerh_ujgh.entity.Carga_academica;
import com.example.sigerh_ujgh.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Carga_academicaRepository extends JpaRepository<Carga_academica, Long> {


    List<Carga_academica> findByEmpleadoId( Long idEmpleado);
    List<Carga_academica> findByEmpleadoAndPeriodoAcademico_ActivoTrue(Empleado empleado);
}
