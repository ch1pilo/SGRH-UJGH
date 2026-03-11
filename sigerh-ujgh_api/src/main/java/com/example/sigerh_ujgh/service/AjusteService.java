package com.example.sigerh_ujgh.service;

import com.example.sigerh_ujgh.entity.Ajuste;
import com.example.sigerh_ujgh.repository.AjusteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AjusteService {

    @Autowired
    private AjusteRepository ajusteRepository;

    // Crear un nuevo ajuste
    public Ajuste registrarAjuste(Ajuste ajuste) {
        ajuste.setProcesado(false); // Por defecto entra como pendiente
        return ajusteRepository.save(ajuste);
    }

    // Listar todos para la tabla del Frontend
    public List<Ajuste> obtenerTodos() {
        return ajusteRepository.findAll();
    }

    // Obtener los pendientes para el cálculo de nómina
    public List<Ajuste> obtenerPendientesDeEmpleado(Long empleadoId) {
        return ajusteRepository.findByEmpleadoIdAndProcesadoFalse(empleadoId);
    }

    // Cuando se genere la nómina, llamaremos a este método para "quemar" el ajuste
    public Ajuste marcarComoProcesado(Long id) {
        Optional<Ajuste> ajusteOpt = ajusteRepository.findById(id);
        if (ajusteOpt.isPresent()) {
            Ajuste ajuste = ajusteOpt.get();
            ajuste.setProcesado(true);
            return ajusteRepository.save(ajuste);
        }
        throw new RuntimeException("Ajuste no encontrado");
    }

    public void eliminarAjuste(Long id) {
        ajusteRepository.deleteById(id);
    }
}