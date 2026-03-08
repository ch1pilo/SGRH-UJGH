package com.example.sigerh_ujgh.controller;

import com.example.sigerh_ujgh.entity.Seguimiento_entrevista;
import com.example.sigerh_ujgh.service.SeguimientoEntrevistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seguimiento_entrevista")
@CrossOrigin(origins = "*")
public class SeguimientoEntrevistaController {

    @Autowired
    private SeguimientoEntrevistaService service;

    // GET /api/seguimiento_entrevista/historial/5
    @GetMapping("/historial/{idEntrevista}")
    public ResponseEntity<List<Seguimiento_entrevista>> obtenerHistorial(@PathVariable Long idEntrevista) {
        return ResponseEntity.ok(service.verHistorial(idEntrevista));
    }

    // POST /api/seguimiento_entrevista
    @PostMapping
    public ResponseEntity<Seguimiento_entrevista> guardarSeguimiento(@RequestBody Seguimiento_entrevista seguimiento) {
        Seguimiento_entrevista nuevo = service.registrarSeguimiento(seguimiento);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }
}