package com.example.sigerh_ujgh.controller;

import com.example.sigerh_ujgh.entity.Facultad;
import com.example.sigerh_ujgh.service.FacultadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("api/facultades")
public class FacultadController {

    @Autowired
    private FacultadService facultadService;

    @PostMapping
    public Facultad guardar(@RequestBody Facultad facultad){
        System.out.println("----------------------------------");
        System.out.println(facultad.getCodigo()+ " " + facultad.getNombre());
        return facultadService.guardarFacultad(facultad);
    }

    @GetMapping
    public List<Facultad> listar(){
        return facultadService.listaFacultades();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Facultad> actualizar(@PathVariable Long id, @RequestBody Facultad facultad){

        Facultad editada = facultadService.actualizarFacultad(id, facultad);
        if (editada != null){
            return ResponseEntity.ok(editada);
        }else {
            return ResponseEntity.notFound().build();
        }
    }
}
