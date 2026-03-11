package com.example.sigerh_ujgh.service;

import com.example.sigerh_ujgh.entity.Rol;
import com.example.sigerh_ujgh.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    public List<Rol> obtenerTodos() {
        return rolRepository.findAll();
    }

    public Rol crearRol(Rol rol) {
        return rolRepository.save(rol);
    }

    public void eliminarRol(Long id) {
        rolRepository.deleteById(id);
    }
}