package com.example.sigerh_ujgh.repository;

import com.example.sigerh_ujgh.entity.Empleado;
import com.example.sigerh_ujgh.entity.Nomina;
import com.example.sigerh_ujgh.entity.Inacistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDate;

@Repository
public interface InasistenciaRepository extends JpaRepository<Inacistencia, Long> {

    // 1. Buscar todas las faltas de un empleado específico
    List<Inacistencia> findByEmpleado(Empleado empleado);

    List<Inacistencia> findByActivo(boolean activo);

    // 3. [IMPORTANTE] Buscar faltas activas de un empleado específico
    // Este es el método que usa tu 'NominaService' para descontar dinero.
    // Traduce a: WHERE id_empleado = ? AND activo = TRUE
    List<Inacistencia> findByEmpleadoAndActivoTrue(Empleado empleado);
    boolean existsByEmpleadoAndFecha(Empleado empleado, LocalDate fecha);
}