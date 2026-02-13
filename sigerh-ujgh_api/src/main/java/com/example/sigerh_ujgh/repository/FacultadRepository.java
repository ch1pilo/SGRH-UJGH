package com.example.sigerh_ujgh.repository;

import com.example.sigerh_ujgh.entity.Facultad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacultadRepository extends JpaRepository<Facultad, Long>{

}
