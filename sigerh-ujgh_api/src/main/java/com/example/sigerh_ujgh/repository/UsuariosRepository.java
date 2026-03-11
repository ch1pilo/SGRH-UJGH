package com.example.sigerh_ujgh.repository;

import com.example.sigerh_ujgh.entity.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuariosRepository extends JpaRepository<Usuarios, Long> {
    Optional<Usuarios> findByUsername(String username);
    boolean existsByUsername(String username);
}