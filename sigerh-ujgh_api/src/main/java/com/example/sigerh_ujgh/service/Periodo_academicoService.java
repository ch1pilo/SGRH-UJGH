package com.example.sigerh_ujgh.service;
import com.example.sigerh_ujgh.entity.Periodo_academico;
import com.example.sigerh_ujgh.repository.Periodo_academicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class Periodo_academicoService {

    @Autowired
    Periodo_academicoRepository periodoAcademicoRepository;

    public List<Periodo_academico> listar(){
        return periodoAcademicoRepository.findAll();
    }

    public Periodo_academico guardar (Periodo_academico periodoAcademico){
        return periodoAcademicoRepository.save(periodoAcademico);
    }

    public Periodo_academico actualizar (Periodo_academico periodoAcademico, Long id){
        Periodo_academico existe = periodoAcademicoRepository.findById(id).orElse(null);
        if (existe != null){
            existe.setActivo(periodoAcademico.isActivo());
            existe.setFecha_fin(periodoAcademico.getFecha_fin());
            existe.setFecha_inicio(periodoAcademico.getFecha_inicio());
            existe.setNombre(periodoAcademico.getNombre());
            return periodoAcademicoRepository.save(existe);
        }
        else {
            return null;
        }
    }
}
