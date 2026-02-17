package com.example.sigerh_ujgh.controller;
import com.example.sigerh_ujgh.entity.Entrevista;
import com.example.sigerh_ujgh.service.EntrevistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/entrevista")
@CrossOrigin(origins = "*")
public class EntrevistaController {

    @Autowired
    private EntrevistaService entrevistaService;

    @GetMapping
    public List<Entrevista> listar(){
        return entrevistaService.listar();
    }

    @PostMapping
    public Entrevista guardar(@RequestBody Entrevista entrevista){
        return entrevistaService.guardar(entrevista);
    }


}
