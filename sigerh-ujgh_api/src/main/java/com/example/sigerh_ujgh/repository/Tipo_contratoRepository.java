package com.example.sigerh_ujgh.repository;
import com.example.sigerh_ujgh.entity.Tipo_contrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Tipo_contratoRepository extends JpaRepository<Tipo_contrato, Long>{
}
