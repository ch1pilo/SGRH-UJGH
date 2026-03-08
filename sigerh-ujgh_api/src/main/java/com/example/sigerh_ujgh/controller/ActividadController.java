package com.example.sigerh_ujgh.controller;

import com.example.sigerh_ujgh.entity.Tipo_actividades_administrativas;
import com.example.sigerh_ujgh.service.ActividadServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/actividades")
@CrossOrigin(origins = "*")
public class ActividadController {

    // Fíjate que aquí uso tu nombre exacto: ActividadServices
    @Autowired
    private ActividadServices actividadServices;

    @GetMapping
    public ResponseEntity<List<Tipo_actividades_administrativas>> getAllActividades() {
        return ResponseEntity.ok(actividadServices.listarTodas());
    }

    @GetMapping("/activas")
    public ResponseEntity<List<Tipo_actividades_administrativas>> getActividadesActivas() {
        return ResponseEntity.ok(actividadServices.listarActivas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tipo_actividades_administrativas> getActividadById(@PathVariable Long id) {
        Optional<Tipo_actividades_administrativas> actividad = actividadServices.buscarPorId(id);
        return actividad.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Tipo_actividades_administrativas> createActividad(@RequestBody Tipo_actividades_administrativas actividad) {
        Tipo_actividades_administrativas nuevaActividad = (Tipo_actividades_administrativas) actividadServices.guardar(actividad);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaActividad);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tipo_actividades_administrativas> updateActividad(@PathVariable Long id, @RequestBody Tipo_actividades_administrativas actividad) {
        if (!actividadServices.buscarPorId(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        actividad.setId(id);
        return ResponseEntity.ok((Tipo_actividades_administrativas) actividadServices.guardar(actividad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActividad(@PathVariable Long id) {
        if (!actividadServices.buscarPorId(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        actividadServices.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}