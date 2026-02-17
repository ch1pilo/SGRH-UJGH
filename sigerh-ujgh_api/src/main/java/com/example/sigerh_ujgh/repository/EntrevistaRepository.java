package com.example.sigerh_ujgh.repository;
import com.example.sigerh_ujgh.entity.Entrevista;
import com.example.sigerh_ujgh.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntrevistaRepository extends JpaRepository<Entrevista, Long>{
    List<Entrevista> findByPersonaAndEstatus(Persona persona, Boolean estatus);
}
