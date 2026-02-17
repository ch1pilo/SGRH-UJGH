package com.example.sigerh_ujgh.service;

import com.example.sigerh_ujgh.entity.Inacistencia;
import com.example.sigerh_ujgh.entity.LoteNomina;
import com.example.sigerh_ujgh.entity.Nomina;
import com.example.sigerh_ujgh.repository.InasistenciaRepository;
import com.example.sigerh_ujgh.repository.LoteNominaRepository;
import com.example.sigerh_ujgh.repository.NominaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LoteNominaService {

    @Autowired
    private LoteNominaRepository repository;
    @Autowired
    private NominaRepository nominaRepository;
    @Autowired
    private InasistenciaRepository inasistenciaRepo;

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

    @Transactional
    public LoteNomina cerrarLoteNomina(Long idLoteNomina) {
        LoteNomina lote = repository.findById(idLoteNomina).orElseThrow();

        // 1. Buscamos todas las nóminas generadas en este lote
        List<Nomina> recibos = nominaRepository.findByLoteNominaId(idLoteNomina);

        for (Nomina recibo : recibos) {
            // Buscamos las inasistencias activas de este empleado y las cerramos
            List<Inacistencia> faltas = inasistenciaRepo.findByEmpleadoAndActivoTrue(recibo.getEmpleado());
            for (Inacistencia f : faltas) {
                f.setActivo(false); // ¡AQUÍ ES DONDE SE MARCAN COMO PAGADAS!
                inasistenciaRepo.save(f);
            }
        }

        lote.setEstatus("CERRADO");
        repository.save(lote);
        return lote;
    }

    public LoteNomina obtenerActivo() {
        return repository.findByEstatus("ABIERTO").orElse(null);
    }
}