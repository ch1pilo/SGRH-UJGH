package com.example.sigerh_ujgh.service;

import com.example.sigerh_ujgh.entity.Usuarios;
import com.example.sigerh_ujgh.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuariosRepository usuariosRepository;

    public List<Usuarios> obtenerTodos() {
        return usuariosRepository.findAll();
    }

    public Usuarios crearUsuario(Usuarios usuario) {
        if (usuariosRepository.existsByUsername(usuario.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }
        if (usuario.getActivo() == null) {
            usuario.setActivo(true);
        }
        return usuariosRepository.save(usuario);
    }

    public Usuarios cambiarEstado(Long id, Boolean activo) {
        Optional<Usuarios> optUsuario = usuariosRepository.findById(id);
        if (optUsuario.isPresent()) {
            Usuarios usuario = optUsuario.get();
            usuario.setActivo(activo);
            return usuariosRepository.save(usuario);
        }
        throw new RuntimeException("Usuario no encontrado");
    }

    public void eliminarUsuario(Long id) {
        usuariosRepository.deleteById(id);
    }
}