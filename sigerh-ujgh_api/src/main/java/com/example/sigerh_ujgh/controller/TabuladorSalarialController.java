package com.example.sigerh_ujgh.controller;

import com.example.sigerh_ujgh.entity.Tabulador_salarial;
import com.example.sigerh_ujgh.service.Tabulador_salarialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tabulador_salarial")
@CrossOrigin("*")
public class TabuladorSalarialController {

    @Autowired
    private Tabulador_salarialService service;

    @GetMapping
    public List<Tabulador_salarial> listar() {
        return service.listar();
    }

    @PostMapping
    public Tabulador_salarial guardar(@RequestBody Tabulador_salarial obj) {
        return service.guardar(obj);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tabulador_salarial> actualizar(@PathVariable Long id, @RequestBody Tabulador_salarial obj) {
        Tabulador_salarial actualizado = service.actualizar(id, obj);
        if (actualizado != null) {
            return ResponseEntity.ok(actualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}