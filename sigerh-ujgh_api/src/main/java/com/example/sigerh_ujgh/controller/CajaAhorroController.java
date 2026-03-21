package com.example.sigerh_ujgh.controller;

import com.example.sigerh_ujgh.dto.CajaAhorroResumenDTO;
import com.example.sigerh_ujgh.entity.MovimientoCajaAhorro;
import com.example.sigerh_ujgh.service.CajaAhorroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/caja_ahorro")
@CrossOrigin("*") // Ajusta esto según tu configuración de CORS
public class CajaAhorroController {

    @Autowired
    private CajaAhorroService cajaAhorroService;

    // Endpoint 1: Llena la tabla principal
    @GetMapping("/resumen")
    public List<CajaAhorroResumenDTO> obtenerResumen() {
        return cajaAhorroService.obtenerResumenGlobal();
    }

    // Endpoint 2: Llena el Modal de Historial
    @GetMapping("/historial/{idEmpleado}")
    public List<MovimientoCajaAhorro> obtenerHistorial(@PathVariable Long idEmpleado) {
        return cajaAhorroService.obtenerHistorialPorEmpleado(idEmpleado);
    }
}