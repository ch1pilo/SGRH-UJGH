package com.example.sigerh_ujgh.repository;

import com.example.sigerh_ujgh.entity.Contrato;
import com.example.sigerh_ujgh.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {
    @Query("SELECT DISTINCT c.empleado FROM Contrato c WHERE c.activo = true")
    List<Empleado> findEmpleadosConContratosActivos();

    boolean existsByEmpleadoIdAndActivoTrue(Long empleadoId);

    List<Contrato> findByEmpleadoIdAndActivoTrue(Long empleadoId);


}