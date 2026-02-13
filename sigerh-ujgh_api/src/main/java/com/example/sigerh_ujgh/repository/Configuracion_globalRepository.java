package com.example.sigerh_ujgh.repository;
import com.example.sigerh_ujgh.entity.Configuracion_global;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Configuracion_globalRepository extends JpaRepository<Configuracion_global, Long>{
    // Método para buscar por clave (ej: "tasa_bcv", "factor_turno_nocturno")
    Optional<Configuracion_global> findByClave(String clave);
}
