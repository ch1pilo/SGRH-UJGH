package com.example.sigerh_ujgh.repository;

import com.example.sigerh_ujgh.entity.Empleado;
import com.example.sigerh_ujgh.entity.Inacistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDate;

@Repository
public interface InasistenciaRepository extends JpaRepository<Inacistencia, Long> {

    List<Inacistencia> findByEmpleado(Empleado empleado);

    List<Inacistencia> findByActivo(boolean activo);
    List<Inacistencia> findByEmpleadoAndActivoTrue(Empleado empleado);
    boolean existsByEmpleadoAndFecha(Empleado empleado, LocalDate fecha);
}