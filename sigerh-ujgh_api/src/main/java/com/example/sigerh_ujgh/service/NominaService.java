package com.example.sigerh_ujgh.service;

import com.example.sigerh_ujgh.entity.*;
import com.example.sigerh_ujgh.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class NominaService {

    private static final Logger log = LoggerFactory.getLogger(NominaService.class);

    @Autowired
    private NominaRepository nominaRepository;
    @Autowired
    private LoteNominaRepository loteNominaRepository;
    @Autowired
    private Carga_academicaRepository cargaRepository;
    @Autowired
    private ContratoRepository contratoRepository;
    @Autowired
    private Configuracion_globalRepository configRepository;
    @Autowired
    private EmpleadoRepository empleadoRepository;
    @Autowired
    private InasistenciaRepository inasistenciaRepo;
    @Autowired
    private Novedad_descuentoRepository novedadRepo;

    public List<Nomina> listarPorLote(Long idLoteNomina) {
        return nominaRepository.findByLoteNominaId(idLoteNomina);
    }

    private BigDecimal obtenerConfig(String clave, BigDecimal valorDefecto) {
        return configRepository.findByClave(clave)
                .map(Configuracion_global::getValor)
                .orElse(valorDefecto);
    }

    @Transactional
    public void calcularNominaMasiva(Long idLoteNomina) {

        log.info("---- INICIO PROCESO DE NÓMINA ----");

        LoteNomina lote = loteNominaRepository.findById(idLoteNomina)
                .orElseThrow(() -> new RuntimeException("Lote no encontrado"));

        if ("CERRADO".equalsIgnoreCase(lote.getEstatus())) {
            throw new RuntimeException("Lote cerrado. No se puede recalcular.");
        }

        if (nominaRepository.existsByLoteNominaId(idLoteNomina)) {
            log.info(">> MODO RECÁLCULO: Borrando recibos anteriores del lote " + idLoteNomina);
            nominaRepository.deleteByLoteNominaId(idLoteNomina);
            nominaRepository.flush();
        }

        // 2. CONFIGURACIONES GLOBALES
        BigDecimal tasaBcv = obtenerConfig("tasa_bcv", new BigDecimal("36.50"));
        BigDecimal factorCestaticket = obtenerConfig("Factor Bono", new BigDecimal("0.0")); // FACTOR POR HORA
        BigDecimal porcSSO = obtenerConfig("porcentaje_sso", new BigDecimal("0.04"));
        BigDecimal porcSPF = obtenerConfig("porcentaje_spf", new BigDecimal("0.005"));
        BigDecimal porcFAOV = obtenerConfig("porcentaje_faov", new BigDecimal("0.01"));

        List<Empleado> empleadosActivos = empleadoRepository.findByEstatus("1");

        // 3. BUCLE PRINCIPAL
        for (Empleado empleado : empleadosActivos) {
            try {
                BigDecimal totalHorasTrabajadas = BigDecimal.ZERO;
                BigDecimal totalDineroDiurnoUsd = BigDecimal.ZERO;
                BigDecimal totalDineroFinUsd = BigDecimal.ZERO;
                BigDecimal totalDineroNocturnoUsd = BigDecimal.ZERO;

                BigDecimal sueldoMensualUsd = BigDecimal.ZERO;
                BigDecimal sueldoQuincenalUsd = BigDecimal.ZERO;
                Contrato contratoActivo = null;

                // ==========================================
                // CÁLCULO 1: SUELDO BASE FIJO (Del tabulador del contrato)
                // ==========================================
                List<Contrato> contratos = contratoRepository.findByEmpleadoIdAndActivoTrue(empleado.getId());
                if (!contratos.isEmpty()) {
                    contratoActivo = contratos.get(0);
                    if (contratoActivo.getIdTabuladorSalarial() != null) {
                        sueldoMensualUsd = BigDecimal.valueOf(contratoActivo.getIdTabuladorSalarial().getSueldo_base());
                        if (sueldoMensualUsd.compareTo(BigDecimal.ZERO) > 0) {
                            sueldoQuincenalUsd = sueldoMensualUsd.divide(new BigDecimal("2"), 2, RoundingMode.HALF_UP);
                        }
                    }
                }

                // ==========================================
                // CÁLCULO 2: PAGO POR HORAS (Carga Académica)
                // ==========================================
                List<Carga_academica> cargas = cargaRepository.findByEmpleadoId(empleado.getId());

                for (Carga_academica carga : cargas) {
                    if (contratoActivo == null) contratoActivo = carga.getId_contrato();

                    if (carga.getId_contrato() != null && carga.getId_contrato().getIdTabuladorSalarial() != null) {
                        System.out.println("\n========== EVALUANDO MATERIA / CARGA ==========");

                        BigDecimal valorHoraBaseUsd = BigDecimal.valueOf(carga.getId_contrato().getIdTabuladorSalarial().getValor_hora());
                        System.out.println("1. Valor Hora Base (Tabulador): $" + valorHoraBaseUsd);

                        BigDecimal horasSemanales = new BigDecimal(carga.getHoras_semanales());
                        System.out.println("2. Horas Semanales de esta carga: " + horasSemanales + " hrs");

                        // Asumimos 2 semanas exactas por quincena
                        BigDecimal horasQuincena = horasSemanales.multiply(new BigDecimal("2"));
                        System.out.println("3. Horas Quincena (Semanal x 2): " + horasQuincena + " hrs");

                        totalHorasTrabajadas = totalHorasTrabajadas.add(horasQuincena);

                        // --- EL PUNTO CRÍTICO: EL MULTIPLICADOR ---
                        BigDecimal multiplicador = carga.getTurno().getFactor_multiplicador();
                        if (multiplicador == null) multiplicador = BigDecimal.ONE;

                        String nombreTurno = carga.getTurno().getNombre() != null ? carga.getTurno().getNombre().toUpperCase() : "DIURNO";
                        System.out.println("4. Turno detectado: " + nombreTurno + " | Multiplicador en BD: " + multiplicador);

                        // Cálculo final de la hora
                        BigDecimal valorHoraFinalUsd = valorHoraBaseUsd.multiply(multiplicador);
                        System.out.println("5. Valor Hora FINAL (Base x Multiplicador): $" + valorHoraFinalUsd);

                        // Subtotal de la materia
                        BigDecimal subtotalMateriaUsd = horasQuincena.multiply(valorHoraFinalUsd);
                        System.out.println("6. Subtotal a pagar por esta materia: $" + subtotalMateriaUsd);

                        // Clasificación en los potes
                        if (nombreTurno.contains("NOCTURNO")) {
                            totalDineroNocturnoUsd = totalDineroNocturnoUsd.add(subtotalMateriaUsd);
                            System.out.println("7. -> Se sumó al pote NOCTURNO. Acumulado: $" + totalDineroNocturnoUsd);
                        } else if (nombreTurno.contains("FIN DE SEMANA") || nombreTurno.contains("SABADO") || nombreTurno.contains("DOMINGO")) {
                            totalDineroFinUsd = totalDineroFinUsd.add(subtotalMateriaUsd);
                            System.out.println("7. -> Se sumó al pote FIN DE SEMANA. Acumulado: $" + totalDineroFinUsd);
                        } else {
                            totalDineroDiurnoUsd = totalDineroDiurnoUsd.add(subtotalMateriaUsd);
                            System.out.println("7. -> Se sumó al pote DIURNO. Acumulado: $" + totalDineroDiurnoUsd);
                        }
                        System.out.println("===============================================");
                    }
                }

                // ==========================================
                // CONVERSIONES A BS E INGRESOS TOTALES
                // ==========================================
                BigDecimal sueldoMensualBs = sueldoMensualUsd.multiply(tasaBcv);
                BigDecimal sueldoQuincenalBs = sueldoQuincenalUsd.multiply(tasaBcv);

                BigDecimal totalDineroDiurnoBs = totalDineroDiurnoUsd.multiply(tasaBcv);
                BigDecimal totalFinBs = totalDineroFinUsd.multiply(tasaBcv);
                BigDecimal totalDineroNocturnoBs = totalDineroNocturnoUsd.multiply(tasaBcv);

                // TU FÓRMULA DE CESTATICKET: Total Horas * Factor Bono * Tasa
                BigDecimal montoCestaticketBs = totalHorasTrabajadas.multiply(factorCestaticket).multiply(tasaBcv);

                BigDecimal totalIngresoAsignacionesBs = sueldoQuincenalBs.add(totalDineroDiurnoBs).add(totalDineroNocturnoBs).add(totalFinBs);

                if (totalIngresoAsignacionesBs.compareTo(BigDecimal.ZERO) == 0 && totalHorasTrabajadas.compareTo(BigDecimal.ZERO) == 0) {
                    continue; // Saltar si no gana nada
                }

                // ==========================================
                // CÁLCULO 3: DEDUCCIONES DE LEY
                // ==========================================
                BigDecimal montoSSO_Bs = BigDecimal.ZERO;
                BigDecimal montoSPF_Bs = BigDecimal.ZERO;
                BigDecimal montoFAOV_Bs = BigDecimal.ZERO;
                BigDecimal totalDescuentosLegalesBs = BigDecimal.ZERO;

                // Las deducciones se calculan sobre el sueldo de contrato (Sueldo Quincenal)
                if (sueldoMensualUsd.compareTo(BigDecimal.ZERO) > 0) {

                    BigDecimal baseLegalSemanalBs = sueldoMensualUsd.multiply(new BigDecimal("12"))
                            .divide(new BigDecimal("52"), 2, RoundingMode.HALF_UP);

                    int lunesMes = lote.getLunes_del_mes() > 0 ? lote.getLunes_del_mes() : 4;

                    montoSSO_Bs = baseLegalSemanalBs.multiply(porcSSO).multiply(new BigDecimal(lunesMes))
                            .divide(new BigDecimal("2"), 2, RoundingMode.HALF_UP);

                    montoSPF_Bs = baseLegalSemanalBs.multiply(porcSPF).multiply(new BigDecimal(lunesMes))
                            .divide(new BigDecimal("2"), 2, RoundingMode.HALF_UP);

                    montoFAOV_Bs = sueldoQuincenalUsd.multiply(porcFAOV).setScale(2, RoundingMode.HALF_UP);

                    totalDescuentosLegalesBs = montoSSO_Bs.add(montoSPF_Bs).add(montoFAOV_Bs);
                }

                // ==========================================
                // CÁLCULO 4: INASISTENCIAS Y DEUDAS
                // ==========================================
                BigDecimal totalDescuentoInasistenciaBs = BigDecimal.ZERO;
                List<Inacistencia> inasistencias = inasistenciaRepo.findByEmpleadoAndActivoTrue(empleado);

                for (Inacistencia inasistencia : inasistencias) {
                    if (inasistencia.getMontoTotal() != null) {
                        totalDescuentoInasistenciaBs = totalDescuentoInasistenciaBs.add(inasistencia.getMontoTotal());
                    }
                    inasistencia.setActivo(false);
                    inasistenciaRepo.save(inasistencia);
                }

                BigDecimal totalDescuentoDeudasBs = BigDecimal.ZERO;
                List<Novedad_descuento> deudas = novedadRepo.findByEmpleadoIdAndProcesadoFalse(empleado.getId());

                for (Novedad_descuento deuda : deudas) {
                    BigDecimal cuota = deuda.getCuota_por_nomina();
                    if (cuota.compareTo(deuda.getSaldo_pendiente()) > 0) {
                        cuota = deuda.getSaldo_pendiente();
                    }
                    totalDescuentoDeudasBs = totalDescuentoDeudasBs.add(cuota);
                    deuda.setSaldo_pendiente(deuda.getSaldo_pendiente().subtract(cuota));
                    if (deuda.getSaldo_pendiente().compareTo(BigDecimal.ZERO) <= 0) {
                        deuda.setProcesado(true);
                    }
                    novedadRepo.save(deuda);
                }

                BigDecimal valorNominaBs = sueldoQuincenalBs.subtract(totalDescuentosLegalesBs);
                if (valorNominaBs.compareTo(BigDecimal.ZERO) < 0) valorNominaBs = BigDecimal.ZERO;

                // ==========================================
                // PREPARAR Y GUARDAR RECIBO
                // ==========================================
                Nomina recibo = new Nomina();
                recibo.setLoteNomina(lote);
                recibo.setEmpleado(empleado);
                if (contratoActivo != null) recibo.setId_contrato(contratoActivo);

                recibo.setTasabcv(tasaBcv);
                recibo.setSueldo_base(sueldoQuincenalBs);
                recibo.setSuedoMensual(sueldoMensualBs);
                recibo.setHora_diurna(totalDineroDiurnoBs);
                recibo.setHora_fin(totalFinBs);
                recibo.setHora_nocturna(totalDineroNocturnoBs);
                recibo.setTotal_ingreso(totalIngresoAsignacionesBs);

                recibo.setValor_cestaticket(factorCestaticket);
                recibo.setMonto_cestaticket(montoCestaticketBs);

                recibo.setPorcentaje_sso(porcSSO);
                recibo.setPorcentaje_spf(porcSPF);
                recibo.setPorcentaje_Faov(porcFAOV);
                recibo.setMonto_sso(montoSSO_Bs);
                recibo.setMonto_spf(montoSPF_Bs);
                recibo.setMonto_faov(montoFAOV_Bs);
                recibo.setTotal_descuento_legales(totalDescuentosLegalesBs);
                recibo.setNomina(valorNominaBs);

                recibo.setMonto_descuento_hora(totalDescuentoInasistenciaBs);
                recibo.setOtros_descuento(totalDescuentoDeudasBs);

                BigDecimal totalDeduccionesBs = totalDescuentosLegalesBs.add(totalDescuentoDeudasBs).add(totalDescuentoInasistenciaBs);
                recibo.setTotal_deducciones(totalDeduccionesBs);

                BigDecimal granTotalBs = totalIngresoAsignacionesBs.add(montoCestaticketBs);
                BigDecimal netoAPagarBs = granTotalBs.subtract(totalDeduccionesBs);

                if (netoAPagarBs.compareTo(BigDecimal.ZERO) < 0) netoAPagarBs = BigDecimal.ZERO;

                recibo.setTotal_bs(netoAPagarBs);
                recibo.setNeto_a_pagar(netoAPagarBs.divide(tasaBcv, 2, RoundingMode.HALF_UP));

                nominaRepository.save(recibo);

                log.info("-> ✅ Guardado: Empleado ID " + empleado.getId() + " | Neto Bs: " + netoAPagarBs);

            } catch (Exception e) {
                log.error("❌ Error procesando empleado ID: " + empleado.getId(), e);
            }
        }

        log.info("---- CÁLCULO DE NÓMINA FINALIZADO ----");
    }
}