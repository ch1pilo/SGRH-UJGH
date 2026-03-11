package com.example.sigerh_ujgh.service;

import com.example.sigerh_ujgh.entity.Usuarios;
import com.example.sigerh_ujgh.repository.UsuariosRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthService {

    private final UsuariosRepository usuariosRepository;

    public AuthService(UsuariosRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
    }

    public Usuarios autenticar(String username, String password) {
        Optional<Usuarios> usuarioOpt = usuariosRepository.findByUsername(username);

        if (usuarioOpt.isPresent()) {
            Usuarios usuario = usuarioOpt.get();

            // Validamos la contraseña y que el usuario esté activo
            if (usuario.getPassword().equals(password) && usuario.getActivo()) {
                return usuario;
            }
        }
        return null; // Credenciales incorrectas o inactivo
    }
}