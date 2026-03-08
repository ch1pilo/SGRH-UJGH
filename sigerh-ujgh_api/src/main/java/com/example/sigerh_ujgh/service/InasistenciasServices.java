package com.example.sigerh_ujgh.service;

import com.example.sigerh_ujgh.entity.*;

import com.example.sigerh_ujgh.repository.InasistenciaRepository;
import com.example.sigerh_ujgh.repository.EmpleadoRepository;
import com.example.sigerh_ujgh.repository.Carga_academicaRepository;
import com.example.sigerh_ujgh.repository.Configuracion_globalRepository; // Esta es la que ya tienes

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class InasistenciasServices {



        @Autowired
        private InasistenciaRepository inasistenciaRepo;

        @Autowired
        private EmpleadoRepository empleadoRepo;

        @Autowired
        private Carga_academicaRepository cargaRepo;

        @Autowired
        private Configuracion_globalRepository configRepo;

        /**
         * Registra una falta, calcula el costo y la guarda.
         */

        public List<Inacistencia> listar() {
            return inasistenciaRepo.findAll();
        }

    @Transactional
    public Inacistencia registrarInasistencia(Long idEmpleado, Integer horas, LocalDate fecha, String observacion) {

        // 1. VALIDAR EMPLEADO
        Empleado empleado = empleadoRepo.findById(idEmpleado)
                .orElseThrow(() -> new RuntimeException("Error: Empleado no encontrado con ID: " + idEmpleado));

        // 2. VALIDAR DUPLICADOS
        if (inasistenciaRepo.existsByEmpleadoAndFecha(empleado, fecha)) {
            throw new RuntimeException("Advertencia: Ya existe una inasistencia en la fecha " + fecha);
        }

        // 3. BUSCAR CARGA ACADÉMICA (Turno y Sueldo)
        Carga_academica carga = cargaRepo.findByEmpleadoAndPeriodoAcademico_ActivoTrue(empleado)
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("El empleado no tiene Carga Académica activa en el periodo actual."));

        // 4. OBTENER VALOR HORA BASE
        if (carga.getId_contrato() == null || carga.getId_contrato().getIdTabuladorSalarial() == null) {
            throw new RuntimeException("Error Config: La carga no tiene contrato o tabulador asociado.");
        }

        Float valorHoraFloat = carga.getId_contrato().getIdTabuladorSalarial().getValor_hora();
        BigDecimal valorHoraBase = BigDecimal.valueOf(valorHoraFloat);

        // =========================================================
        // 5. DETERMINAR EL FACTOR DIRECTAMENTE DESDE LA TABLA TURNO
        // =========================================================
        Turno turno = carga.getTurno();

        // Obtenemos el factor directo (Ej: 1.00, 1.80 o 2.00)
        BigDecimal factor = turno.getFactor_multiplicador();
        if (factor == null) {
            factor = BigDecimal.ONE; // Por seguridad, si está vacío en BD usamos 1.00
        }

        // Obtenemos el nombre exacto del turno para guardarlo en la observación
        String detalleTurno = turno.getNombre() != null ? turno.getNombre() : "Turno Normal";

        // 6. CALCULAR MONTO TOTAL ($)
        BigDecimal horasBD = new BigDecimal(horas);

        BigDecimal montoTotalCalculado = horasBD
                .multiply(valorHoraBase)
                .multiply(factor)
                .setScale(2, java.math.RoundingMode.HALF_UP);

        // 7. GUARDAR
        Inacistencia inasistencia = new Inacistencia();
        inasistencia.setEmpleado(empleado);
        inasistencia.setFecha(fecha);
        inasistencia.setHoras(horas);
        inasistencia.setFactorAplicado(factor);
        inasistencia.setMontoTotal(montoTotalCalculado);

        // Si no envían observación, ponemos una automática mucho más limpia
        if (observacion == null || observacion.isEmpty()) {
            inasistencia.setObservacion("Inasistencia " + detalleTurno + " (Factor: " + factor + ")");
        } else {
            inasistencia.setObservacion(observacion);
        }

        return inasistenciaRepo.save(inasistencia);
    }
        // --- HELPER PARA LEER CONFIGURACIÓN GLOBAL ---
        private BigDecimal getValorConfig(String clave, BigDecimal valorDefecto) {
            Optional<Configuracion_global> config = configRepo.findByClave(clave);
            if (config.isPresent()) {
                return config.get().getValor(); // Tu entidad ya devuelve BigDecimal según el SQL
            }
            return valorDefecto;
        }
    }