package com.example.sigerh_ujgh.service;

import com.example.sigerh_ujgh.dto.CajaAhorroResumenDTO;
import com.example.sigerh_ujgh.entity.Empleado;
import com.example.sigerh_ujgh.entity.MovimientoCajaAhorro;
import com.example.sigerh_ujgh.repository.EmpleadoRepository;
import com.example.sigerh_ujgh.repository.MovimientoCajaAhorroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CajaAhorroService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private MovimientoCajaAhorroRepository movimientoRepository;

    public List<CajaAhorroResumenDTO> obtenerResumenGlobal() {
        List<Empleado> empleados = empleadoRepository.findAll();
        List<CajaAhorroResumenDTO> resumen = new ArrayList<>();

        for (Empleado emp : empleados) {
            CajaAhorroResumenDTO dto = new CajaAhorroResumenDTO();
            dto.setIdEmpleado(emp.getId());
            dto.setCedula(emp.getPersona().getCedula());
            dto.setNombre(emp.getPersona().getNombre() + " " + emp.getPersona().getApellido());

            // Buscamos el último movimiento de este empleado
            Optional<MovimientoCajaAhorro> ultimoMov = movimientoRepository.findUltimoMovimientoByEmpleado(emp.getId());

            if (ultimoMov.isPresent()) {
                dto.setUltimoAporte(ultimoMov.get().getAporteEmpleado());
                dto.setAcumuladoTotal(ultimoMov.get().getAcumuladoTotal());
                // Aquí asumo que si tiene dinero es porque está activa.
                // Si tienes un campo en Empleado tipo "afiliadoCajaAhorro", úsalo aquí.
                dto.setActiva(ultimoMov.get().getAcumuladoTotal().compareTo(BigDecimal.ZERO) > 0);
            } else {
                dto.setUltimoAporte(BigDecimal.ZERO);
                dto.setAcumuladoTotal(BigDecimal.ZERO);
                dto.setActiva(false);
            }

            resumen.add(dto);
        }
        return resumen;
    }

    public List<MovimientoCajaAhorro> obtenerHistorialPorEmpleado(Long idEmpleado) {
        // En tu repositorio de MovimientoCajaAhorro debes crear un método que busque todos los movimientos por idEmpleado
        return movimientoRepository.findByEmpleadoIdOrderByFechaOperacionDesc(idEmpleado);
    }
}