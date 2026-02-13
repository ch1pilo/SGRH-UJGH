package com.example.sigerh_ujgh.controller;

import com.example.sigerh_ujgh.entity.Configuracion_global;
import com.example.sigerh_ujgh.service.Configuracion_globalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/configuracion_global")
@CrossOrigin("*")
public class ConfiguracionGlobalController {

    @Autowired
    private Configuracion_globalService service;

    @GetMapping
    public List<Configuracion_global> listar() {
        return service.listar();
    }

    @PostMapping
    public Configuracion_global guardar(@RequestBody Configuracion_global obj) {
        return service.Guardar(obj);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Configuracion_global> actualizar(@PathVariable Long id, @RequestBody Configuracion_global obj) {
        Configuracion_global actualizado = service.actualizar(id, obj);
        if (actualizado != null) {
            return ResponseEntity.ok(actualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}