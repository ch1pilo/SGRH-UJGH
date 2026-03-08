package com.example.sigerh_ujgh.service;

import com.example.sigerh_ujgh.entity.Seguimiento_entrevista;
import com.example.sigerh_ujgh.repository.SeguimientoEntrevistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeguimientoEntrevistaService {

    @Autowired
    private SeguimientoEntrevistaRepository repository;

    // Registrar un nuevo evento en el historial (Ej: "Se pospuso la entrevista")
    public Seguimiento_entrevista registrarSeguimiento(Seguimiento_entrevista seguimiento) {
        return repository.save(seguimiento);
    }

    // Ver el historial completo de una entrevista
    public List<Seguimiento_entrevista> verHistorial(Long idEntrevista) {
        return repository.buscarHistorialPorEntrevista(idEntrevista);
    }
}
