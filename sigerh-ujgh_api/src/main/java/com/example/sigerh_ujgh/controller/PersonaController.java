package com.example.sigerh_ujgh.controller;

import com.example.sigerh_ujgh.service.PersonaService;
import com.example.sigerh_ujgh.entity.Persona;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/persona")
@CrossOrigin("*")
public class PersonaController {

    @Autowired
    private PersonaService personaService;

    @PostMapping
    public Persona guardarPersona(@RequestBody Persona persona){
        System.out.println("______________________________");
        System.out.println(persona.getNombre() + " " + persona.getApellido());
        return personaService.guardar_persona(persona);
    }

    @GetMapping
    public List<Persona> listarPresona(){
        return personaService.listaPersona();
    }

    @PutMapping("/{id}")
    public Persona actualizar(@PathVariable long id,@RequestBody Persona persona){
        return personaService.Actualizar_persona(id, persona);
    }
}
