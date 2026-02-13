package com.example.sigerh_ujgh.controller;

import com.example.sigerh_ujgh.entity.Carga_academica;
import com.example.sigerh_ujgh.service.Carga_academicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carga_academica")
@CrossOrigin("*")
public class CargaAcademicaController {

    @Autowired
    private Carga_academicaService service;

    @GetMapping
    public List<Carga_academica> listar() {
        return service.listar();
    }

    @PostMapping
    public Carga_academica guardar(@RequestBody Carga_academica obj) {
        return service.guardar(obj);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Carga_academica> actualizar(@PathVariable Long id, @RequestBody Carga_academica obj) {
        Carga_academica actualizado = service.actualizar(id, obj);
        if (actualizado != null) {
            return ResponseEntity.ok(actualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}