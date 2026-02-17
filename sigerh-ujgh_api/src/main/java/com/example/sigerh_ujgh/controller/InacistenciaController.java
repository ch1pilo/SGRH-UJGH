package com.example.sigerh_ujgh.controller;
import com.example.sigerh_ujgh.entity.Facultad;
import com.example.sigerh_ujgh.entity.Inacistencia;
import com.example.sigerh_ujgh.service.InasistenciasServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inasistencias")
@CrossOrigin(origins = "*")
public class InacistenciaController {

    @Autowired
    private InasistenciasServices inasistenciaService;

    // Listar todas (opcional, por si quieres verlas)
    @GetMapping
    public List<Inacistencia> listar() {
        return inasistenciaService.listar();
    }

    // Guardar: Recibe la Entidad directa, saca los datos y llama al cálculo
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Inacistencia inasistencia) {
        try {
            // Extraemos los datos básicos del objeto que viene en el JSON
            // Nota: Asumimos que dentro de 'inasistencia' viene el objeto 'empleado' con su ID
            Long idEmpleado = inasistencia.getEmpleado().getId();

            Inacistencia nuevaFalta = inasistenciaService.registrarInasistencia(
                    idEmpleado,
                    inasistencia.getHoras(),
                    inasistencia.getFecha(),
                    inasistencia.getObservacion()
            );

            return ResponseEntity.ok(nuevaFalta);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error interno: " + e.getMessage());
        }


    }

}