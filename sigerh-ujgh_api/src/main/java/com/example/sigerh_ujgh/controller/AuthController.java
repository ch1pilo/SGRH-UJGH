package com.example.sigerh_ujgh.controller;

import com.example.sigerh_ujgh.dto.LoginRequest;
import com.example.sigerh_ujgh.entity.Rol;
import com.example.sigerh_ujgh.entity.Usuarios;
import com.example.sigerh_ujgh.entity.Vista;
import com.example.sigerh_ujgh.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Usuarios usuario = authService.autenticar(request.getUsername(), request.getPassword());

        if (usuario != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("id", usuario.getId());
            response.put("username", usuario.getUsername());

            // Extraer solo los nombres de los roles y las rutas de las vistas
            Set<String> nombresRoles = new HashSet<>();
            Set<String> rutasPermitidas = new HashSet<>();

            for (Rol rol : usuario.getRoles()) {
                nombresRoles.add(rol.getNombre());
                for (Vista vista : rol.getVistasPermitidas()) {
                    rutasPermitidas.add(vista.getRuta()); // Ej: "/empleados", "/nomina"
                }
            }

            response.put("roles", nombresRoles);
            response.put("vistasPermitidas", rutasPermitidas);

            return ResponseEntity.ok(response);
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("mensaje", "Usuario o contraseña incorrectos");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
}











