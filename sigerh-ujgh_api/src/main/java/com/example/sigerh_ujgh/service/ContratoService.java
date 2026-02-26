package com.example.sigerh_ujgh.service;

import com.example.sigerh_ujgh.dto.EmpleadosContratosDTO;
import com.example.sigerh_ujgh.entity.Contrato;
import com.example.sigerh_ujgh.entity.Empleado;
import com.example.sigerh_ujgh.repository.ContratoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ContratoService {

    @Autowired
    private ContratoRepository repository;

    // CRUD BÁSICO
    public List<Contrato> listarTodos() {
        return repository.findAll();
    }

    public Contrato guardar(Contrato contrato) {
        // Aquí podrías validar fechas o lógica de negocio si es necesario


        boolean tieneContratoActivo = repository.existsByEmpleadoIdAndActivoTrue(contrato.getEmpleado().getId());

        if (tieneContratoActivo) {
            // Si ya tiene uno, disparamos un error y detenemos todo
            throw new RuntimeException("El empleado ya tiene un contrato activo. Debe finalizar el actual antes de asignar uno nuevo.");
        }

        return repository.save(contrato);
    }

    // --- LÓGICA DE AGRUPACIÓN (LO QUE PEDISTE) ---
    public List<EmpleadosContratosDTO> listarContratosAgrupados() {
        List<Contrato> todos = repository.findAll();

        // 1. Agrupar por ID de Empleado
        Map<Long, List<Contrato>> mapa = todos.stream()
                .collect(Collectors.groupingBy(c -> c.getEmpleado().getId()));

        List<EmpleadosContratosDTO> resultado = new ArrayList<>();

        // 2. Construir el DTO para cada empleado encontrado
        mapa.forEach((idEmp, listaContratos) -> {
            if (!listaContratos.isEmpty()) {
                Empleado emp = listaContratos.get(0).getEmpleado();

                // Ordenar contratos por fecha inicio descendente (el más nuevo primero)
                listaContratos.sort(Comparator.comparing(Contrato::getFechaInicio).reversed());

                EmpleadosContratosDTO dto = new EmpleadosContratosDTO();
                dto.setEmpleado(emp);
                dto.setContratos(listaContratos);
                dto.setCantidadContratos(listaContratos.size());
                dto.setUltimoContrato(listaContratos.get(0)); // El primero es el más reciente

                resultado.add(dto);
            }
        });

        return resultado;
    }
}