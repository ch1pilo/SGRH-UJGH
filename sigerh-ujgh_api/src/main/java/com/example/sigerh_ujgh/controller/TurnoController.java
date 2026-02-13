package com.example.sigerh_ujgh.controller;

import com.example.sigerh_ujgh.entity.Turno;
import com.example.sigerh_ujgh.service.TurnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/turno")
@CrossOrigin("*")
public class TurnoController {

    @Autowired
    private TurnoService service;

    @GetMapping
    public List<Turno> listar() {
        return service.listar();
    }

    @PostMapping
    public Turno guardar(@RequestBody Turno obj) {
        return service.guardar(obj);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Turno> actualizar(@PathVariable Long id, @RequestBody Turno obj) {
        Turno actualizado = service.actualizar(id, obj);
        if (actualizado != null) {
            return ResponseEntity.ok(actualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}