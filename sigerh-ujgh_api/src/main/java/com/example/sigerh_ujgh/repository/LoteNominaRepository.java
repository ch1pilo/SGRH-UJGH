package com.example.sigerh_ujgh.repository;

import com.example.sigerh_ujgh.entity.LoteNomina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LoteNominaRepository extends JpaRepository<LoteNomina, Long> {
    Optional<LoteNomina> findByEstatus(String estatus);
}