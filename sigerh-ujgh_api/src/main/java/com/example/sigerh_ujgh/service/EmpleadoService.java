package com.example.sigerh_ujgh.service;

import com.example.sigerh_ujgh.entity.Empleado;
import com.example.sigerh_ujgh.entity.Entrevista; // Usamos Entrevista
import com.example.sigerh_ujgh.entity.Persona;
import com.example.sigerh_ujgh.repository.EmpleadoRepository;
import com.example.sigerh_ujgh.repository.EntrevistaRepository; // Repo de Entrevista
import com.example.sigerh_ujgh.repository.PersonaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private EntrevistaRepository entrevistaRepository; // <--- AQUÍ BUSCAMOS LA ENTREVISTA

    public List<Empleado> listar() {
        return empleadoRepository.findAll();
    }

    public Empleado guardar(Empleado empleado) {
        return empleadoRepository.save(empleado);
    }

    @Transactional
    public Empleado actualizarEstatus(Long idEmpleado, String nuevoEstatus) {
        // 1. Buscamos al empleado
        Empleado empleado = empleadoRepository.findById(idEmpleado)
                .orElseThrow(() -> new RuntimeException("Error: No existe el empleado con ID " + idEmpleado));

        // 2. Actualizamos solo el estatus
        empleado.setEstatus(nuevoEstatus);

        // 3. Guardamos los cambios
        return empleadoRepository.save(empleado);
    }

    @Transactional
    public Empleado registrarPorIdPersona(Long idPersona, LocalDate fechaIngreso) {

        Persona persona = personaRepository.findById(idPersona)
                .orElseThrow(() -> new RuntimeException("Error: No existe persona con ID " + idPersona));

        // 2. BUSCAMOS LA ENTREVISTA DE ESA PERSONA Y LA CERRAMOS
        // Buscamos las entrevistas de esta persona que estén activas (true)
        List<Entrevista> entrevistasPendientes = entrevistaRepository.findByPersonaAndEstatus(persona, true);

        for (Entrevista entrevista : entrevistasPendientes) {
            entrevista.setEstatus(false); // La pasamos a FALSE para que no salga más
            entrevistaRepository.save(entrevista);
        }

        // 3. CREAMOS EL EMPLEADO
        Empleado nuevoEmpleado = new Empleado();
        nuevoEmpleado.setPersona(persona);
        nuevoEmpleado.setFechaIngreso(fechaIngreso);
        nuevoEmpleado.setEstatus("1"); // Activo en nómina

        return empleadoRepository.save(nuevoEmpleado);
    }

    public void eliminar(Long id) {
        empleadoRepository.deleteById(id);
    }
}