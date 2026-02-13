package com.example.sigerh_ujgh.controller;

import com.example.sigerh_ujgh.entity.Nomina;
import com.example.sigerh_ujgh.service.NominaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nomina")
@CrossOrigin("*")
public class NominaController {

    @Autowired
    private NominaService service;

    // EL BOTÓN DE PAGO: Dispara el cálculo masivo
    @PostMapping("/calcular/{idLoteNomina}")
    public ResponseEntity<String> calcularNomina(@PathVariable Long idLoteNomina) {
        try {
            service.calcularNominaMasiva(idLoteNomina);
            return ResponseEntity.ok("Nomina calculada exitosamente para el Lote ID: " + idLoteNomina);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error al calcular: " + e.getMessage());
        }
    }

    // VER LOS RECIBOS: Muestra cuánto cobró cada uno en ese lote
    @GetMapping("/lote/{idLoteNomina}")
    public List<Nomina> listarPorLote(@PathVariable Long idLoteNomina) {
        return service.listarPorLote(idLoteNomina);
    }
}