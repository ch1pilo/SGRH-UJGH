package com.example.sigerh_ujgh.repository;
import com.example.sigerh_ujgh.entity.Detalle_nomina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Detalle_nominaRepository extends JpaRepository<Detalle_nomina, Long> {
    List<Detalle_nomina> findByNominaId(Long idNomina);
}
