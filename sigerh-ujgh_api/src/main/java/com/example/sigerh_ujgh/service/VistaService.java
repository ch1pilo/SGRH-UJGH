package com.example.sigerh_ujgh.service;

import com.example.sigerh_ujgh.entity.Vista;
import com.example.sigerh_ujgh.repository.VistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VistaService {

    @Autowired
    private VistaRepository vistaRepository;

    public List<Vista> obtenerTodas() {
        return vistaRepository.findAll();
    }

    public Vista crearVista(Vista vista) {
        return vistaRepository.save(vista);
    }

    public void eliminarVista(Long id) {
        vistaRepository.deleteById(id);
    }
}