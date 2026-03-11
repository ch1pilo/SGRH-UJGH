package com.example.sigerh_ujgh.controller;

import com.example.sigerh_ujgh.entity.Vista;
import com.example.sigerh_ujgh.service.VistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vistas")
@CrossOrigin(origins = "*")
public class VistaController {

    @Autowired
    private VistaService vistaService;

    @GetMapping
    public List<Vista> listarTodas( ) {
        return vistaService.obtenerTodas();
    }

    @PostMapping
    public ResponseEntity<Vista> crear(@RequestBody Vista vista) {
        return ResponseEntity.ok(vistaService.crearVista(vista));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        vistaService.eliminarVista(id);
        return ResponseEntity.ok().build();
    }
}