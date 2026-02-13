package com.example.sigerh_ujgh.service;

import com.example.sigerh_ujgh.entity.Turno;
import com.example.sigerh_ujgh.repository.TurnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TurnoService {

    @Autowired
    private TurnoRepository turnoRepository;

    public List<Turno> listar(){
        return turnoRepository.findAll();
    }

    public Turno guardar(Turno turno){
        return turnoRepository.save(turno);
    }

    public Turno actualizar(long id, Turno turno){
        Turno existe = turnoRepository.findById(id).orElse(null);
        if (existe != null){
            existe.setNombre(turno.getNombre());
            existe.setNocturno(turno.isNocturno());
            existe.setFin_de_semana(turno.isFin_de_semana());
            existe.setHora_inicio(turno.getHora_inicio());
            existe.setHora_fin(turno.getHora_fin());
            return turnoRepository.save(existe);
        }
        else {
            return null;
        }
    }
}
