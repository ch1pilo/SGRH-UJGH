package com.example.sigerh_ujgh.service;

import com.example.sigerh_ujgh.entity.Novedad_descuento;
import com.example.sigerh_ujgh.repository.Novedad_descuentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NovedaDescuentoService {

    @Autowired
    private Novedad_descuentoRepository repository;

    public List<Novedad_descuento> listar() {
        return repository.findAll();
    }

    // Listar deudas pendientes de un empleado
    public List<Novedad_descuento> listarPendientesPorEmpleado(Long idEmpleado) {
        return repository.findByEmpleadoIdAndProcesadoFalse(idEmpleado);
    }

    public Novedad_descuento guardar(Novedad_descuento obj) {
        if (obj.getId() == null) {
            obj.setProcesado(false); // Siempre nace como no cobrada
        }
        return repository.save(obj);
    }
}