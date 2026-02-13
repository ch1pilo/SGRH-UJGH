package com.example.sigerh_ujgh.service;

import com.example.sigerh_ujgh.entity.LoteNomina;
import com.example.sigerh_ujgh.repository.LoteNominaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LoteNominaService {

    @Autowired
    private LoteNominaRepository repository;

    public List<LoteNomina> listar() {
        return repository.findAll();
    }

    public LoteNomina guardar(LoteNomina obj) {
        // Validar que no haya otro lote ABIERTO antes de crear uno nuevo
        if (obj.getId() == null) { // Solo al crear
            Optional<LoteNomina> abierto = repository.findByEstatus("ABIERTO");
            if (abierto.isPresent()) {
                throw new RuntimeException("Ya existe una nómina abierta. Debe cerrarla antes de crear otra.");
            }
            obj.setEstatus("ABIERTO");
        }
        return repository.save(obj);
    }

    public LoteNomina cerrarLote(Long id) {
        LoteNomina lote = repository.findById(id).orElse(null);
        if (lote != null) {
            lote.setEstatus("CERRADO");
            return repository.save(lote);
        }
        return null;
    }

    public LoteNomina obtenerActivo() {
        return repository.findByEstatus("ABIERTO").orElse(null);
    }
}