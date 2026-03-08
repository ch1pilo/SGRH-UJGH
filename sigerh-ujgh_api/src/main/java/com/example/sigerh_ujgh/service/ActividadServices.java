package com.example.sigerh_ujgh.service;

import com.example.sigerh_ujgh.entity.Tipo_actividades_administrativas;
import com.example.sigerh_ujgh.repository.ActividadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActividadServices {

    @Autowired
    private ActividadRepository actividadRepository;

    // Listar TODAS las actividades (incluyendo inactivas si se necesita auditar)
    public List<Tipo_actividades_administrativas> listarTodas() {
        return actividadRepository.findAll();
    }

    // Listar SOLO las activas (para llenar el <select> en el Frontend)
    public List<Tipo_actividades_administrativas> listarActivas() {
        return actividadRepository.findByActivoTrue();
    }

    // Buscar una actividad por ID
    public Optional<Tipo_actividades_administrativas> buscarPorId(Long id) {
        return actividadRepository.findById(id);
    }

    // Crear o Actualizar una actividad
    public Object guardar(Tipo_actividades_administrativas actividad) {
        return actividadRepository.save(actividad);
    }

    // Borrado lógico (Desactivar)
    public void desactivar(Long id) {
        Optional<Tipo_actividades_administrativas> actividadOpt = actividadRepository.findById(id);
        if (actividadOpt.isPresent()) {
            Tipo_actividades_administrativas actividad = actividadOpt.get();
            actividad.setActivo(false); // No la borramos, solo la ocultamos
            actividadRepository.save(actividad);
        }
    }
}
