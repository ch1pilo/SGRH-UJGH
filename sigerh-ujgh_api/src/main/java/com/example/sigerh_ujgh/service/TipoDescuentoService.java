package com.example.sigerh_ujgh.service;

import com.example.sigerh_ujgh.entity.Tipo_descuento;
import com.example.sigerh_ujgh.repository.TipoDescuentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TipoDescuentoService {

    @Autowired
    private TipoDescuentoRepository repository;

    public List<Tipo_descuento> listar() {
        return repository.findAll();
    }

    public Tipo_descuento guardar(Tipo_descuento obj) {
        return repository.save(obj);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}