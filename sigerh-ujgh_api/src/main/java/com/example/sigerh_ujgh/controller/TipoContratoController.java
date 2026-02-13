package com.example.sigerh_ujgh.controller;

import com.example.sigerh_ujgh.entity.Tipo_contrato;
import com.example.sigerh_ujgh.service.Tipo_contratoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipo_contrato")
@CrossOrigin("*")
public class TipoContratoController {

    @Autowired
    private Tipo_contratoService service;

    @GetMapping
    public List<Tipo_contrato> listar() {
        return service.listar();
    }

    @PostMapping
    public Tipo_contrato guardar(@RequestBody Tipo_contrato obj) {
        return service.guardar(obj);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tipo_contrato> actualizar(@PathVariable Long id, @RequestBody Tipo_contrato obj) {
        Tipo_contrato actualizado = service.actualizar(id, obj);
        if (actualizado != null) {
            return ResponseEntity.ok(actualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}