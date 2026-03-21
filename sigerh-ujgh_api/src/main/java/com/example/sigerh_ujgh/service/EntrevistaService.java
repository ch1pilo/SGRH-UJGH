package com.example.sigerh_ujgh.service;
import com.example.sigerh_ujgh.entity.Entrevista;
import com.example.sigerh_ujgh.repository.EntrevistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EntrevistaService {
    @Autowired
    EntrevistaRepository entrevistaRepository;

    public List<Entrevista> listar(){
        return entrevistaRepository.findAll();
    }

    public Entrevista guardar (Entrevista entrevista){
        return entrevistaRepository.save(entrevista);
    }

    public Entrevista editar(Long id, Entrevista datosNuevos) {
        // 1. Buscamos la entrevista original en la base de datos
        Entrevista entrevistaExistente = entrevistaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la entrevista con ID: " + id));

        // 2. Actualizamos SOLO los campos que nos interesan (Fecha y Observación)
        // OJO: Asegúrate de que los nombres de los "setters" coincidan con los tuyos
        entrevistaExistente.setFecha_programada(datosNuevos.getFecha_programada());
        entrevistaExistente.setObservacion(datosNuevos.getObservacion());

        // 3. Guardamos los cambios y retornamos la entrevista actualizada
        return entrevistaRepository.save(entrevistaExistente);
    }

    public Entrevista actualizar (Long id, Entrevista entrevista){
        Entrevista existe = entrevistaRepository.findById(id).orElse(null);
        if (existe != null ){
            existe.setFecha_programada(entrevista.getFecha_programada());
            existe.setObservacion(entrevista.getObservacion());
            existe.setPersona(entrevista.getPersona());
            return entrevistaRepository.save(existe);
        }
        else{
            return null;
        }
    }
}
