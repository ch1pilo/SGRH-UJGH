package com.example.sigerh_ujgh.service;
import com.example.sigerh_ujgh.entity.Facultad;
import com.example.sigerh_ujgh.repository.FacultadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FacultadService {

    @Autowired
    private FacultadRepository facultadRepository;

    public Facultad guardarFacultad(Facultad facultad) {
        return facultadRepository.save(facultad);
    }

    public List<Facultad> listaFacultades() {
        return facultadRepository.findAll();
    }

    public Facultad actualizarFacultad(Long id, Facultad facultad) {
        Facultad existe = facultadRepository.findById(id).orElse(null);
        if (existe != null) {

            existe.setNombre(facultad.getNombre());
            existe.setCodigo(facultad.getCodigo());
            existe.setEstatus(facultad.getEstatus());
            return facultadRepository.save(existe);
        }
        else {
            return null;
        }
    }
}
