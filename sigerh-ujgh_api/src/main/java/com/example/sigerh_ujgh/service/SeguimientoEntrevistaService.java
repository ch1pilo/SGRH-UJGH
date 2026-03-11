package com.example.sigerh_ujgh.service;

import com.example.sigerh_ujgh.entity.Entrevista;
import com.example.sigerh_ujgh.entity.Seguimiento_entrevista;
import com.example.sigerh_ujgh.repository.EntrevistaRepository;
import com.example.sigerh_ujgh.repository.SeguimientoEntrevistaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeguimientoEntrevistaService {

    @Autowired
    private SeguimientoEntrevistaRepository seguimientoRepo;

    @Autowired
    private EntrevistaRepository entrevistaRepo;

    // 1. CREAR UNA RE-ENTREVISTA (y cerrar las anteriores)
    @Transactional
    public Seguimiento_entrevista programarReentrevista(Seguimiento_entrevista nuevoSeguimiento) {
        Long idPadre = nuevoSeguimiento.getId_entrevista().getId();

        // A. Buscar si hay re-entrevistas anteriores pendientes y cerrarlas ("COMPLETADA")
        List<Seguimiento_entrevista> pendientes = seguimientoRepo.findPendientesByEntrevista(idPadre);
        for (Seguimiento_entrevista anterior : pendientes) {
            anterior.setEstado("COMPLETADA");
            seguimientoRepo.save(anterior);
        }

        // B. Guardar la nueva re-entrevista
        nuevoSeguimiento.setEstado("PENDIENTE"); // Estado inicial
        return seguimientoRepo.save(nuevoSeguimiento);
    }

    // 2. FINALIZAR TODO EL PROCESO (Pone en 0 al padre)
    @Transactional
    public Entrevista finalizarProcesoEntrevista(Long idEntrevista) {
        // A. Buscar la entrevista padre
        Entrevista entrevistaPadre = entrevistaRepo.findById(idEntrevista)
                .orElseThrow(() -> new RuntimeException("Entrevista no encontrada"));

        // B. Poner el estatus en false (Estado 0 como pediste)
        entrevistaPadre.setEstatus(false);
        entrevistaRepo.save(entrevistaPadre);

        // C. (Opcional) Si quedó un seguimiento "PENDIENTE", lo cerramos a "COMPLETADA"
        List<Seguimiento_entrevista> pendientes = seguimientoRepo.findPendientesByEntrevista(idEntrevista);
        for (Seguimiento_entrevista pendiente : pendientes) {
            pendiente.setEstado("COMPLETADA");
            seguimientoRepo.save(pendiente);
        }

        return entrevistaPadre;
    }

    // 3. OBTENER HISTORIAL DE UNA ENTREVISTA
    public List<Seguimiento_entrevista> obtenerHistorial(Long idEntrevista) {
        return seguimientoRepo.findByIdEntrevista(idEntrevista);
    }
}