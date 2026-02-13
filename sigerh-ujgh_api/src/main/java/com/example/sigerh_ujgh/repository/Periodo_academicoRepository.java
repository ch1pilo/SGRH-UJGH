package com.example.sigerh_ujgh.repository;
import com.example.sigerh_ujgh.entity.Periodo_academico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Periodo_academicoRepository extends JpaRepository<Periodo_academico, Long>{
}
