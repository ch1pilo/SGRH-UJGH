package com.example.sigerh_ujgh.repository;

import com.example.sigerh_ujgh.entity.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Long>{
}
