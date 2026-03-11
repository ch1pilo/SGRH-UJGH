package com.example.sigerh_ujgh.controller;

import com.example.sigerh_ujgh.entity.Entrevista;
import com.example.sigerh_ujgh.entity.Seguimiento_entrevista;
import com.example.sigerh_ujgh.service.SeguimientoEntrevistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seguimiento")
@CrossOrigin("*")
public class SeguimientoEntrevistaController {

    @Autowired
    private SeguimientoEntrevistaService service;

    // Crear re-entrevista
    @PostMapping("/reentrevistar")
    public ResponseEntity<Seguimiento_entrevista> reentrevistar(@RequestBody Seguimiento_entrevista seguimiento) {
        return ResponseEntity.ok(service.programarReentrevista(seguimiento));
    }

    // Finalizar proceso completo (Padre a false)
    @PutMapping("/finalizar/{idEntrevista}")
    public ResponseEntity<Entrevista> finalizarProceso(@PathVariable Long idEntrevista) {
        return ResponseEntity.ok(service.finalizarProcesoEntrevista(idEntrevista));
    }

    // Ver historial de re-entrevistas
    @GetMapping("/historial/{idEntrevista}")
    public ResponseEntity<List<Seguimiento_entrevista>> verHistorial(@PathVariable Long idEntrevista) {
        return ResponseEntity.ok(service.obtenerHistorial(idEntrevista));
    }
}