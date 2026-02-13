package com.example.sigerh_ujgh.service;
import com.example.sigerh_ujgh.entity.Carga_academica;
import com.example.sigerh_ujgh.repository.Carga_academicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class Carga_academicaService {

    @Autowired
    Carga_academicaRepository cargaAcademicaRepository;

    public List<Carga_academica> listar(){
        return cargaAcademicaRepository.findAll();
    }

    public Carga_academica guardar(Carga_academica cargaAcademica){
        return cargaAcademicaRepository.save(cargaAcademica);
    }

    public Carga_academica actualizar (Long id, Carga_academica cargaAcademica){
        Carga_academica existe = cargaAcademicaRepository.findById(id).orElse(null);
        if (existe != null ){
            existe.setEmpleado(cargaAcademica.getEmpleado());
            existe.setHoras_semanales(cargaAcademica.getHoras_semanales());
            existe.setObservacion(cargaAcademica.getObservacion());
            existe.setTurno(cargaAcademica.getTurno());
            existe.setPeriodoAcademico(cargaAcademica.getPeriodoAcademico());
            return cargaAcademicaRepository.save(existe);
        }
        else {
            return null;
        }
    }
}
