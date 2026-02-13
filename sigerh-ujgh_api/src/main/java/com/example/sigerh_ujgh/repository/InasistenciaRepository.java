package com.example.sigerh_ujgh.repository;

import com.example.sigerh_ujgh.entity.Inasistencia;
import com.example.sigerh_ujgh.entity.Empleado;
import com.example.sigerh_ujgh.entity.Nomina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDate;

@Repository
public interface InasistenciaRepository extends JpaRepository<Inasistencia, Long> {

    // 1. Buscar todas las faltas de un empleado específico
    List<Inasistencia> findByEmpleado(Empleado empleado);

    // 2. Buscar todas las faltas asociadas a una nómina (Para generar el reporte de pago)
    List<Inasistencia> findByNomina(Nomina nomina);

    // 3. Validación de Seguridad (Para evitar duplicados desde n8n)
    // Devuelve true si el empleado ya tiene una falta registrada en esa fecha
    boolean existsByEmpleadoAndFecha(Empleado empleado, LocalDate fecha);
}