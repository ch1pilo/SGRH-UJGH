package com.example.sigerh_ujgh.service;
import com.example.sigerh_ujgh.entity.Tipo_contrato;
import com.example.sigerh_ujgh.repository.Tipo_contratoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service

public class Tipo_contratoService {
    @Autowired
    Tipo_contratoRepository tipoContratoRepository;

    public List<Tipo_contrato> listar(){
        return tipoContratoRepository.findAll();
    }

    public Tipo_contrato guardar(Tipo_contrato tipoContrato){
        return tipoContratoRepository.save(tipoContrato);
    }

    public Tipo_contrato actualizar (Long id, Tipo_contrato tipoContrato){
        Tipo_contrato existe = tipoContratoRepository.findById(id).orElse(null);
        if(existe != null){
            existe.setDescripcion(tipoContrato.getDescripcion());
            existe.setNombre(tipoContrato.getNombre());
            tipoContratoRepository.save(existe);
        }
        else {
            return null;
        }
        //me puede dar error
        return existe;
    }
}
