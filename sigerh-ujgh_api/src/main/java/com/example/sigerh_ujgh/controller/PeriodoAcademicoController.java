package com.example.sigerh_ujgh.controller;

import com.example.sigerh_ujgh.entity.Periodo_academico;
import com.example.sigerh_ujgh.service.Periodo_academicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/periodo_academico")
@CrossOrigin("*")
public class PeriodoAcademicoController {

    @Autowired
    private Periodo_academicoService service;

    @GetMapping
    public List<Periodo_academico> listar() {
        return service.listar();
    }

    @PostMapping
    public Periodo_academico guardar(@RequestBody Periodo_academico obj) {
        return service.guardar(obj);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Periodo_academico> actualizar(@PathVariable Long id, @RequestBody Periodo_academico obj) {
        Periodo_academico actualizado = service.actualizar(obj, id);
        if (actualizado != null) {
            return ResponseEntity.ok(actualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}