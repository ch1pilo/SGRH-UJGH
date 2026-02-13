package com.example.sigerh_ujgh.service;
import com.example.sigerh_ujgh.entity.Configuracion_global;
import com.example.sigerh_ujgh.repository.Configuracion_globalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class Configuracion_globalService {

    @Autowired
    Configuracion_globalRepository configuracionGlobalRepository;

    public List<Configuracion_global> listar(){
        return configuracionGlobalRepository.findAll();
    }

    public Configuracion_global Guardar (Configuracion_global configuracionGlobal){
        return configuracionGlobalRepository.save(configuracionGlobal);

    }

    public Configuracion_global actualizar (Long id, Configuracion_global configuracionGlobal){
        Configuracion_global existe = configuracionGlobalRepository.findById(id).orElse(null);
        if (existe != null){
            existe.setClave(configuracionGlobal.getClave());
            existe.setDescripcion(configuracionGlobal.getDescripcion());
            existe.setValor(configuracionGlobal.getValor());
            return configuracionGlobalRepository.save(existe);
        }
        else{
            return null;
        }
    }
}
