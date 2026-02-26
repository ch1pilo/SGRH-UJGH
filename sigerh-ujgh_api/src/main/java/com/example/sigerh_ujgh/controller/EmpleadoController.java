package com.example.sigerh_ujgh.controller;

import com.example.sigerh_ujgh.entity.Empleado;
import com.example.sigerh_ujgh.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/empleados")
@CrossOrigin(origins = "*") // Permite la conexión desde React
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    // 1. Listar
    @GetMapping
    public List<Empleado> listar() {
        return empleadoService.listar();
    }

    // 2. Guardar Normal
    @PostMapping
    public Empleado guardar(@RequestBody Empleado empleado) {
        return empleadoService.guardar(empleado);
    }

    // 3. Eliminar
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        empleadoService.eliminar(id);
    }

    @PutMapping("/{id}/estatus")
    public ResponseEntity<?> actualizarEstatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            System.out.println("-----------entrando al controladorr----------");
            String nuevoEstatus = request.get("estatus");
            Empleado empleadoActualizado = empleadoService.actualizarEstatus(id, nuevoEstatus);
            return ResponseEntity.ok(empleadoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/contratar")
    public ResponseEntity<?> contratar(@RequestBody Map<String, Object> payload) {
        try {
            // Validar que enviaron el ID de la persona
            if (payload.get("idPersona") == null) {
                return ResponseEntity.badRequest().body("Error: Falta el idPersona.");
            }

            Long idPersona = Long.valueOf(payload.get("idPersona").toString());

            // Validar fecha (o usar la de hoy)
            LocalDate fechaIngreso = LocalDate.now();
            if (payload.get("fechaIngreso") != null) {
                fechaIngreso = LocalDate.parse(payload.get("fechaIngreso").toString());
            }

            // Llamar al servicio
            Empleado nuevo = empleadoService.registrarPorIdPersona(idPersona, fechaIngreso);

            return ResponseEntity.ok(nuevo);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}