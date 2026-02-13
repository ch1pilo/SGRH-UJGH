package com.example.sigerh_ujgh.repository;
import com.example.sigerh_ujgh.entity.Tabulador_salarial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Tabulador_salarialRepository extends JpaRepository<Tabulador_salarial, Long>{
}
