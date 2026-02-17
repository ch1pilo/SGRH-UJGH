package com.example.sigerh_ujgh.controller;

import com.example.sigerh_ujgh.entity.LoteNomina;
import com.example.sigerh_ujgh.service.LoteNominaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lotes-nomina")
@CrossOrigin("*") // Permite que el frontend (React/Angular/Vue) se conecte
public class LoteNominaController {

    @Autowired
    private LoteNominaService service;

    @GetMapping
    public List<LoteNomina> listar() {
        return service.listar();
    }

    @PostMapping
    public ResponseEntity<LoteNomina> crear(@RequestBody LoteNomina obj) {
        return ResponseEntity.ok(service.guardar(obj));
    }

    @PutMapping("/cerrar/{id}")
    public ResponseEntity<LoteNomina> cerrar(@PathVariable Long id) {
        LoteNomina lote = service.cerrarLoteNomina(id);
        if (lote != null) return ResponseEntity.ok(lote);
        return ResponseEntity.notFound().build();
    }
}