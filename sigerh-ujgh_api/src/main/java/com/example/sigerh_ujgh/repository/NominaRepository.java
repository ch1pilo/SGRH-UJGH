package com.example.sigerh_ujgh.repository;
import com.example.sigerh_ujgh.entity.Nomina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NominaRepository extends JpaRepository<Nomina, Long> {
    List<Nomina> findByLoteNominaId(Long idLoteNomina);

    // También necesitarás esta para ver el historial de un empleado:
    List<Nomina> findByEmpleadoId(Long idEmpleado);

    void deleteByLoteNominaId(Long idLoteNomina);

    // En NominaRepository.java
    boolean existsByLoteNominaId(Long idLoteNomina);
}
