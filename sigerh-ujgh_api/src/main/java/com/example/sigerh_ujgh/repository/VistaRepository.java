package com.example.sigerh_ujgh.repository;

import com.example.sigerh_ujgh.entity.Vista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VistaRepository extends JpaRepository<Vista, Long>{
}
