package com.example.sigerh_ujgh.repository;

import com.example.sigerh_ujgh.entity.Ajuste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AjusteRepository extends JpaRepository<Ajuste, Long> {

    // Lista todos los ajustes de un empleado
    List<Ajuste> findByEmpleadoId(Long empleadoId);

    // ESTA ES LA JOYA DE LA CORONA: Busca solo los ajustes que aún NO se han cobrado/descontado
    List<Ajuste> findByEmpleadoIdAndProcesadoFalse(Long empleadoId);
}