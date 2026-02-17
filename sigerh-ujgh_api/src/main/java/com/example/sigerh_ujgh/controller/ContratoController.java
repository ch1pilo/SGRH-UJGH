package com.example.sigerh_ujgh.controller;

import com.example.sigerh_ujgh.dto.EmpleadosContratosDTO;
import com.example.sigerh_ujgh.entity.Contrato;
import com.example.sigerh_ujgh.service.ContratoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contratos")
@CrossOrigin("*")
public class ContratoController {

    @Autowired
    private ContratoService service;

    // Endpoint para guardar (Crear Contrato)
    @PostMapping
    public ResponseEntity<Contrato> guardar(@RequestBody Contrato contrato) {
        return ResponseEntity.ok(service.guardar(contrato));
    }

    // Endpoint normal (lista plana)
    @GetMapping
    public List<Contrato> listar() {
        return service.listarTodos();
    }

    // --- NUEVO ENDPOINT PARA LA VISTA PRINCIPAL ---
    // Devuelve los empleados con sus contratos anidados
    @GetMapping("/agrupados")
    public List<EmpleadosContratosDTO> listarAgrupados() {
        return service.listarContratosAgrupados();
    }
}