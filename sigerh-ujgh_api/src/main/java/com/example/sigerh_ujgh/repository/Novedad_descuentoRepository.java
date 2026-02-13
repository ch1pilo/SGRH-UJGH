package com.example.sigerh_ujgh.repository;

import com.example.sigerh_ujgh.entity.Novedad_descuento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository

public interface Novedad_descuentoRepository extends JpaRepository<Novedad_descuento, Long>{
    List<Novedad_descuento> findByEmpleadoIdAndProcesadoFalse(Long idEmpleado);


}
