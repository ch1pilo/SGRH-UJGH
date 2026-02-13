package com.example.sigerh_ujgh.service;

import com.example.sigerh_ujgh.entity.Facultad;
import com.example.sigerh_ujgh.entity.Persona;
import com.example.sigerh_ujgh.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PersonaService {


    @Autowired
    private PersonaRepository personaRepository;

    public Persona guardar_persona(Persona persona){
        return personaRepository.save(persona);
    }

    public List<Persona> listaPersona() {
        return personaRepository.findAll();
    }

    public Persona Actualizar_persona(long id, Persona persona){
        Persona existe = personaRepository.findById(id).orElse(null);

        if (existe != null){
            existe.setNombre(persona.getNombre());
            existe.setApellido(persona.getApellido());
            existe.setCedula(persona.getCedula());
            existe.setCorreo(persona.getCorreo());
            existe.setDireccion(persona.getDireccion());
            existe.setTelefono(persona.getTelefono());
            existe.setFecha_nacimiento(persona.getFecha_nacimiento());
            return personaRepository.save(existe);
        }
        else {
            return null;
        }
    }
}
