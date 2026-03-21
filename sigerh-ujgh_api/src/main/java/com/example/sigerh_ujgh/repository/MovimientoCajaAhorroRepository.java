package com.example.sigerh_ujgh.repository;

import com.example.sigerh_ujgh.entity.MovimientoCajaAhorro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovimientoCajaAhorroRepository extends JpaRepository<MovimientoCajaAhorro, Long> {

    // Esta magia busca todos los movimientos de un empleado, los ordena por fecha de más nuevo a más viejo, y solo te da el primero (El saldo actual)
    @Query(value = "SELECT * FROM movimiento_caja_ahorro WHERE id_empleado = :idEmpleado ORDER BY fecha_operacion DESC LIMIT 1", nativeQuery = true)
    Optional<MovimientoCajaAhorro> findUltimoMovimientoByEmpleado(@Param("idEmpleado") Long idEmpleado);

    List<MovimientoCajaAhorro> findByEmpleadoIdOrderByFechaOperacionDesc(Long idEmpleado);

}