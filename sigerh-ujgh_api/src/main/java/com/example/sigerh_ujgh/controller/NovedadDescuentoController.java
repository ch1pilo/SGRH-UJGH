package com.example.sigerh_ujgh.controller;

import com.example.sigerh_ujgh.entity.Novedad_descuento;
import com.example.sigerh_ujgh.service.NovedaDescuentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/novedades")
@CrossOrigin("*")
public class NovedadDescuentoController {

    @Autowired
    private NovedaDescuentoService service;

    @GetMapping
    public List<Novedad_descuento> listar() {
        return service.listar();
    }

    @GetMapping("/pendientes/{idEmpleado}")
    public List<Novedad_descuento> listarPendientes(@PathVariable Long idEmpleado) {
        return service.listarPendientesPorEmpleado(idEmpleado);
    }

    @PostMapping
    public ResponseEntity<Novedad_descuento> guardar(@RequestBody Novedad_descuento obj) {
        return ResponseEntity.ok(service.guardar(obj));
    }
}