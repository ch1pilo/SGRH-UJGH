package com.example.sigerh_ujgh.service;

import com.example.sigerh_ujgh.entity.Empleado;
import com.example.sigerh_ujgh.entity.Persona;
import com.example.sigerh_ujgh.repository.EmpleadoRepository;
import com.example.sigerh_ujgh.repository.PersonaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;
    @Autowired
    private PersonaRepository personaRepository;

    public List<Empleado> listar() {
        return empleadoRepository.findAll();
    }

    public Empleado guardar(Empleado empleado) {
        return empleadoRepository.save(empleado);
    }

    @Transactional
    public Empleado registrarPorIdPersona(Long idPersona, LocalDate fechaIngreso) {

        // 1. Validar que la persona exista
        Persona persona = personaRepository.findById(idPersona)
                .orElseThrow(() -> new RuntimeException("No existe ninguna persona con el ID: " + idPersona));

        // 2. Validar que no sea ya empleado
        if (empleadoRepository.existsByPersonaId(idPersona)) {
            throw new RuntimeException("Esta persona ya está registrada como empleado activo.");
        }

        // 3. Crear el Empleado
        Empleado nuevo = new Empleado();
        nuevo.setPersona(persona);
        nuevo.setFechaIngreso(fechaIngreso);
        nuevo.setEstatus("1"); // 1 = Activo por defecto

        return empleadoRepository.save(nuevo);
    }

    public void eliminar(Long id) {
        empleadoRepository.deleteById(id);
    }


}