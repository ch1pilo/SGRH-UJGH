package com.example.sigerh_ujgh.controller;

import com.example.sigerh_ujgh.entity.Carga_administrativa;
import com.example.sigerh_ujgh.entity.Configuracion_global;
import com.example.sigerh_ujgh.service.CargaAdministrativaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carga_administrativa")
@CrossOrigin(origins = "*")
public class CargaAdministrativaController {

    @Autowired
    private CargaAdministrativaService service;

    // Obtener actividades de una carga específica (GET /api/carga_administrativa/carga/5)
    @GetMapping("/carga/{idCarga}")
    public ResponseEntity<List<Carga_administrativa>> getActividadesPorCarga(@PathVariable Long idCarga) {
        return ResponseEntity.ok(service.listarPorCargaAcademica(idCarga));
    }

    @GetMapping
    public List<Carga_administrativa> listar() {
        return service.listar();
    }

    // Asignar una nueva actividad (POST /api/carga_administrativa)
    @PostMapping
    public ResponseEntity<Carga_administrativa> asignarActividad(@RequestBody Carga_administrativa cargaAdmin) {
        Carga_administrativa nueva = service.guardar(cargaAdmin);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    // Eliminar una asignación (DELETE /api/carga_administrativa/2)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAsignacion(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}