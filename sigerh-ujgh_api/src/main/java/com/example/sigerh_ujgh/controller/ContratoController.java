package com.example.sigerh_ujgh.controller;

import com.example.sigerh_ujgh.entity.Contrato;
import com.example.sigerh_ujgh.service.ContratoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/contratos")
@CrossOrigin(origins = "http://localhost:5173")
public class ContratoController {

    @Autowired
    private ContratoService contratoService;

    @GetMapping
    public List<Contrato> listar() {
        return contratoService.listar();
    }

    @PostMapping
    public Contrato guardar(@RequestBody Contrato contrato) {
        return contratoService.guardar(contrato);
    }
}

