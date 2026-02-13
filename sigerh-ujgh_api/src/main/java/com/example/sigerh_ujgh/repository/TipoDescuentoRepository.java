package com.example.sigerh_ujgh.repository;

import com.example.sigerh_ujgh.entity.Tipo_descuento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TipoDescuentoRepository extends JpaRepository <Tipo_descuento, Long> {
}
