package com.example.sigerh_ujgh.service;

import com.example.sigerh_ujgh.entity.Contrato;
import com.example.sigerh_ujgh.repository.ContratoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ContratoService {

    @Autowired
    private ContratoRepository contratoRepository;

    public List<Contrato> listar() {
        return contratoRepository.findAll();
    }

    public Contrato guardar(Contrato contrato) {
        return contratoRepository.save(contrato);
    }
}
