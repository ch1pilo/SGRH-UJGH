package com.example.sigerh_ujgh.service;
import com.example.sigerh_ujgh.entity.Tabulador_salarial;
import com.example.sigerh_ujgh.repository.Tabulador_salarialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Tabulador_salarialService {

    @Autowired
    Tabulador_salarialRepository tabuladorSalarialRepository;

    public List<Tabulador_salarial> listar(){
        return tabuladorSalarialRepository.findAll();
    }

    public Tabulador_salarial guardar(Tabulador_salarial tabuladorSalarial){
        return tabuladorSalarialRepository.save(tabuladorSalarial);
    }

    public Tabulador_salarial actualizar(Long id, Tabulador_salarial tabuladorSalarial){
        Tabulador_salarial existe = tabuladorSalarialRepository.findById(id).orElse(null);
        if (existe != null){
            existe.setNivel(tabuladorSalarial.getNivel());
            existe.setSueldo_base(tabuladorSalarial.getSueldo_base());
            existe.setValor_hora(tabuladorSalarial.getValor_hora());
            return tabuladorSalarialRepository.save(existe);
        }
        else {
            return null;
        }
    }
}
