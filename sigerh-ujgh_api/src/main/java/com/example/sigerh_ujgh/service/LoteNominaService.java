package com.example.sigerh_ujgh.service;

import com.example.sigerh_ujgh.dto.ReciboNominaDTO;
import com.example.sigerh_ujgh.entity.Inacistencia;
import com.example.sigerh_ujgh.entity.LoteNomina;
import com.example.sigerh_ujgh.entity.Nomina;
import com.example.sigerh_ujgh.repository.InasistenciaRepository;
import com.example.sigerh_ujgh.repository.LoteNominaRepository;
import com.example.sigerh_ujgh.repository.NominaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LoteNominaService {

    @Autowired
    private LoteNominaRepository repository;
    @Autowired
    private NominaRepository nominaRepository;
    @Autowired
    private InasistenciaRepository inasistenciaRepo;

    public List<LoteNomina> listar() {
        return repository.findAll();
    }

    public LoteNomina guardar(LoteNomina obj) {
        // Validar que no haya otro lote ABIERTO antes de crear uno nuevo
        if (obj.getId() == null) { // Solo al crear
            Optional<LoteNomina> abierto = repository.findByEstatus("ABIERTO");
            if (abierto.isPresent()) {
                throw new RuntimeException("Ya existe una nómina abierta. Debe cerrarla antes de crear otra.");
            }
            obj.setEstatus("ABIERTO");
        }
        return repository.save(obj);
    }

    @Transactional
    public LoteNomina cerrarLoteNomina(Long idLoteNomina) {
        LoteNomina lote = repository.findById(idLoteNomina).orElseThrow();

        // 1. Buscamos todas las nóminas generadas en este lote
        List<Nomina> recibos = nominaRepository.findByLoteNominaId(idLoteNomina);

        for (Nomina recibo : recibos) {
            // Buscamos las inasistencias activas de este empleado y las cerramos
            List<Inacistencia> faltas = inasistenciaRepo.findByEmpleadoAndActivoTrue(recibo.getEmpleado());
            for (Inacistencia f : faltas) {
                f.setActivo(false); // ¡AQUÍ ES DONDE SE MARCAN COMO PAGADAS!
                inasistenciaRepo.save(f);
            }
        }

        lote.setEstatus("CERRADO");
        repository.save(lote);
        return lote;
    }

    public List<ReciboNominaDTO> obtenerReporteNomina(Long idLoteNomina) {
        List<Nomina> nominas = nominaRepository.findByLoteNominaId(idLoteNomina);

        return nominas.stream().map(n -> {
            ReciboNominaDTO dto = new ReciboNominaDTO();

            // Datos Generales
            dto.setIdNomina(n.getId());
            dto.setTasaBcv(n.getLoteNomina().getTasa_bcv());
            dto.setPeriodo("Lote #" + n.getLoteNomina().getId() + " - " + n.getLoteNomina().getEstatus());

            // Datos del Empleado (Asumiendo que Empleado tiene getPersona())
            if (n.getEmpleado() != null && n.getEmpleado().getPersona() != null) {
                dto.setCedula(n.getEmpleado().getPersona().getCedula());
                dto.setNombreCompleto(n.getEmpleado().getPersona().getNombre() + " " + n.getEmpleado().getPersona().getApellido());
            }

            if (n.getId_contrato() != null && n.getId_contrato().getIdTipoContrato() != null) {
                dto.setCargoContrato(n.getId_contrato().getIdTipoContrato().getNombre());
            } else {
                dto.setCargoContrato("Sin Contrato Específico");
            }

            // Asignaciones
            dto.setSueldoBase(n.getSueldo_base());
            dto.setHorasDiurnas(n.getHora_diurna());
            dto.setHorasNocturnas(n.getHora_nocturna());
            dto.setHorasFinSemana(n.getHora_fin());
            dto.setBonoCestaticket(n.getMonto_cestaticket());
            // Sumamos Ingresos brutos + Cestaticket
            dto.setTotalAsignaciones(n.getTotal_ingreso().add(n.getMonto_cestaticket()));

            // Deducciones Ley
            dto.setMontoSSO(n.getMonto_sso());
            dto.setMontoSPF(n.getMonto_spf());
            dto.setMontoFAOV(n.getMonto_faov());
            dto.setTotalDeduccionesLey(n.getTotal_descuento_legales());

            // Otras Deducciones
            dto.setInasistencias(n.getMonto_descuento_hora());
            dto.setOtrasDeudas(n.getOtros_descuento());
            // Sumamos inasistencias + deudas
            dto.setTotalOtrasDeducciones(n.getMonto_descuento_hora().add(n.getOtros_descuento()));

            // Totales Finales
            dto.setTotalDeducciones(n.getTotal_deducciones());
            dto.setNetoAPagarBs(n.getTotal_bs());
            dto.setNetoAPagarUsd(n.getNeto_a_pagar()); // Asumiendo que esta columna guarda el total en Dólares

            return dto;
        }).collect(Collectors.toList());
    }

    public LoteNomina obtenerActivo() {
        return repository.findByEstatus("ABIERTO").orElse(null);
    }
}