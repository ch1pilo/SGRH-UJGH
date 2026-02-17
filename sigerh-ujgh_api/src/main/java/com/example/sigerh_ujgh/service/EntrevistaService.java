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
