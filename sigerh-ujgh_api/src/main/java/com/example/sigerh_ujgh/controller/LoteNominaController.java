package com.example.sigerh_ujgh.controller;

import com.example.sigerh_ujgh.dto.ReciboNominaDTO;
import com.example.sigerh_ujgh.entity.LoteNomina;
import com.example.sigerh_ujgh.service.CorreoService;
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

    @Autowired
    private CorreoService correoService;
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

    @PostMapping("/{id}/enviar-correos")
    public ResponseEntity<?> enviarCorreosDelLote(@PathVariable Long id) {
        try {
            correoService.enviarCorreosPorLote(id);
            return ResponseEntity.ok("Correos enviados exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}