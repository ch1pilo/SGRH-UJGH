package com.example.sigerh_ujgh.controller;

import com.example.sigerh_ujgh.entity.Ajuste;
import com.example.sigerh_ujgh.service.AjusteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ajustes")
@CrossOrigin("*")
public class AjusteController {

    @Autowired
    private AjusteService ajusteService;

    @GetMapping
    public List<Ajuste> listarTodos() {
        return ajusteService.obtenerTodos();
    }

    @GetMapping("/empleado/{empleadoId}/pendientes")
    public List<Ajuste> listarPendientes(@PathVariable Long empleadoId) {
        return ajusteService.obtenerPendientesDeEmpleado(empleadoId);
    }

    @PostMapping
    public ResponseEntity<Ajuste> crear(@RequestBody Ajuste ajuste) {
        Ajuste nuevoAjuste = ajusteService.registrarAjuste(ajuste);
        return ResponseEntity.ok(nuevoAjuste);
    }

    @PutMapping("/{id}/procesar")
    public ResponseEntity<Ajuste> marcarProcesado(@PathVariable Long id) {
        return ResponseEntity.ok(ajusteService.marcarComoProcesado(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ajusteService.eliminarAjuste(id);
        return ResponseEntity.ok().build();
    }
}