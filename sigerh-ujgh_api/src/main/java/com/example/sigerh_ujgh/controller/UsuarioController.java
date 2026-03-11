package com.example.sigerh_ujgh.controller;

import com.example.sigerh_ujgh.entity.Usuarios;
import com.example.sigerh_ujgh.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin("*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<Usuarios> listarTodos() {
        return usuarioService.obtenerTodos();
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Usuarios usuario) {
        try {
            Usuarios nuevoUsuario = usuarioService.crearUsuario(usuario);
            return ResponseEntity.ok(nuevoUsuario);
        } catch (RuntimeException e) {
            // Retornamos un JSON con el error para que React pueda leer el "mensaje"
            return ResponseEntity.badRequest().body("{\"mensaje\": \"" + e.getMessage() + "\"}");
        }
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Usuarios> cambiarEstado(@PathVariable Long id, @RequestParam Boolean activo) {
        return ResponseEntity.ok(usuarioService.cambiarEstado(id, activo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.ok().build();
    }
}