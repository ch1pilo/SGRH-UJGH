package com.example.sigerh_ujgh.service;

import com.example.sigerh_ujgh.entity.Carga_administrativa;
import com.example.sigerh_ujgh.repository.CargaAdministrativaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CargaAdministrativaService {

    @Autowired
    private CargaAdministrativaRepository repository;

    // Guardar una nueva asignación
    public Carga_administrativa guardar(Carga_administrativa carga) {
        return repository.save(carga);
    }

    // Listar por carga académica
    public List<Carga_administrativa> listarPorCargaAcademica(Long idCarga) {
        return repository.buscarPorCargaId(idCarga);
    }

    public  List<Carga_administrativa> listar(){
        return repository.findAll();
    }

    // Eliminar
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}