package com.example.sigerh_ujgh.service;

import com.example.sigerh_ujgh.dto.DetalleReciboDTO;
import com.example.sigerh_ujgh.dto.ReciboNominaDTO;
import com.example.sigerh_ujgh.entity.*;
import com.example.sigerh_ujgh.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @Autowired
    private Detalle_nominaRepository detalleNominaRepo;
    @Autowired
    private AjusteRepository ajusteRepository;
    @Autowired
    private MovimientoCajaAhorroRepository movimientoCajaAhorroRepository;

    public List<Nomina> listarPorLote(Long idLoteNomina) {
        return nominaRepository.findByLoteNominaId(idLoteNomina);
    }

    private BigDecimal obtenerConfig(String clave, BigDecimal valorDefecto) {
        return configRepository.findByClave(clave)
                .map(Configuracion_global::getValor)
                .orElse(valorDefecto);
    }

    public List<ReciboNominaDTO> obtenerReporteNomina(Long idLoteNomina) {
        List<Nomina> nominas = nominaRepository.findByLoteNominaId(idLoteNomina);

        return nominas.stream().map(n -> {
            ReciboNominaDTO dto = new ReciboNominaDTO();

            // --- 1. DATOS GENERALES ---
            dto.setIdNomina(n.getId());
            dto.setPeriodoPago(n.getLoteNomina().getDescripcion() + " - " + n.getLoteNomina().getFechaInicio());

            if (n.getEmpleado() != null && n.getEmpleado().getPersona() != null) {
                dto.setCedula(n.getEmpleado().getPersona().getCedula());
                dto.setNombreCompleto(n.getEmpleado().getPersona().getNombre() + " " + n.getEmpleado().getPersona().getApellido());
            }

            if (n.getId_contrato() != null && n.getId_contrato().getIdTipoContrato() != null) {
                dto.setCargo(n.getId_contrato().getIdTipoContrato().getNombre());
            } else {
                dto.setCargo("Sin Contrato Específico");
            }

            // --- 2. TOTALES FINALES ---
            dto.setSueldoBaseBs(n.getSueldo_base());

            // Asumiendo que Total_ingreso ya tiene los bonos + sueldo base. Le sumamos el Cestaticket.
            BigDecimal totalAsig = (n.getTotal_ingreso() != null ? n.getTotal_ingreso() : BigDecimal.ZERO)
                    .add(n.getMonto_cestaticket() != null ? n.getMonto_cestaticket() : BigDecimal.ZERO);
            dto.setTotalAsignaciones(totalAsig);

            dto.setTotalDeducciones(n.getTotal_deducciones());
            dto.setNetoAPagar(n.getTotal_bs()); // o n.getNeto_a_pagar() dependiendo de si pagas en Bs o USD

            // --- 3. LISTA DINÁMICA DE ASIGNACIONES (Lo que suma) ---
            List<DetalleReciboDTO> asignaciones = new ArrayList<>();

            if (n.getHora_diurna() != null && n.getHora_diurna().compareTo(BigDecimal.ZERO) > 0) {
                DetalleReciboDTO d = new DetalleReciboDTO();
                d.setConcepto("Bono Horas Diurnas");
                d.setMonto(n.getHora_diurna());
                asignaciones.add(d);
            }
            if (n.getHora_nocturna() != null && n.getHora_nocturna().compareTo(BigDecimal.ZERO) > 0) {
                DetalleReciboDTO d = new DetalleReciboDTO();
                d.setConcepto("Bono Horas Nocturnas");
                d.setMonto(n.getHora_nocturna());
                asignaciones.add(d);
            }
            if (n.getHora_fin() != null && n.getHora_fin().compareTo(BigDecimal.ZERO) > 0) {
                DetalleReciboDTO d = new DetalleReciboDTO();
                d.setConcepto("Bono Fin de Semana");
                d.setMonto(n.getHora_fin());
                asignaciones.add(d);
            }
            if (n.getMonto_cestaticket() != null && n.getMonto_cestaticket().compareTo(BigDecimal.ZERO) > 0) {
                DetalleReciboDTO d = new DetalleReciboDTO();
                d.setConcepto("Cestaticket (Alimentación)");
                d.setMonto(n.getMonto_cestaticket());
                asignaciones.add(d);
            }
            dto.setAsignaciones(asignaciones);

            // --- 4. LISTA DINÁMICA DE DEDUCCIONES (Lo que resta) ---
            List<DetalleReciboDTO> deducciones = new ArrayList<>();

            if (n.getMonto_sso() != null && n.getMonto_sso().compareTo(BigDecimal.ZERO) > 0) {
                DetalleReciboDTO d = new DetalleReciboDTO();
                d.setConcepto("S.S.O.");
                d.setMonto(n.getMonto_sso());
                deducciones.add(d);
            }
            if (n.getMonto_spf() != null && n.getMonto_spf().compareTo(BigDecimal.ZERO) > 0) {
                DetalleReciboDTO d = new DetalleReciboDTO();
                d.setConcepto("Paro Forzoso (R.P.F)");
                d.setMonto(n.getMonto_spf());
                deducciones.add(d);
            }
            if (n.getMonto_faov() != null && n.getMonto_faov().compareTo(BigDecimal.ZERO) > 0) {
                DetalleReciboDTO d = new DetalleReciboDTO();
                d.setConcepto("F.A.O.V.");
                d.setMonto(n.getMonto_faov());
                deducciones.add(d);
            }
            if (n.getMonto_descuento_hora() != null && n.getMonto_descuento_hora().compareTo(BigDecimal.ZERO) > 0) {
                DetalleReciboDTO d = new DetalleReciboDTO();
                d.setConcepto("Inasistencias/Faltas");
                d.setMonto(n.getMonto_descuento_hora());
                deducciones.add(d);
            }
            if (n.getOtros_descuento() != null && n.getOtros_descuento().compareTo(BigDecimal.ZERO) > 0) {
                DetalleReciboDTO d = new DetalleReciboDTO();
                d.setConcepto("Préstamos y Adelantos");
                d.setMonto(n.getOtros_descuento());
                deducciones.add(d);
            }
            if (n.getMontoCajaAhorro() != null && n.getMontoCajaAhorro().compareTo(BigDecimal.ZERO) > 0) {
                DetalleReciboDTO d = new DetalleReciboDTO();
                d.setConcepto("Caja de Ahorros (10%)");
                d.setMonto(n.getMontoCajaAhorro());
                deducciones.add(d);
            }
            dto.setDeducciones(deducciones);

            return dto;
        }).collect(Collectors.toList());
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
        BigDecimal porcCajaAhorro = obtenerConfig("porcentaje_caja_ahorro", new BigDecimal("0.10"));
        BigDecimal factorCestaticket = obtenerConfig("Factor Bono", new BigDecimal("0.0")); // FACTOR POR HORA
        BigDecimal porcSSO = obtenerConfig("porcentaje_sso", new BigDecimal("0.04"));
        BigDecimal porcSPF = obtenerConfig("porcentaje_spf", new BigDecimal("0.005"));
        BigDecimal porcFAOV = obtenerConfig("porcentaje_faov", new BigDecimal("0.01"));

        List<Empleado> empleadosActivos = empleadoRepository.findByEstatus("1");

        // 3. BUCLE PRINCIPAL
        for (Empleado empleado : empleadosActivos) {
            try {

                List<Detalle_nomina> detallesTemporales = new ArrayList<>();

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

                        Detalle_nomina detCarga = new Detalle_nomina();
                        detCarga.setTipoDetalle("ASIGNACION_CARGA");
                        detCarga.setCargaAcademica(carga);
                        // Lo pasamos a Bs de una vez para el recibo
                        detCarga.setValor(subtotalMateriaUsd.multiply(tasaBcv));
                        detallesTemporales.add(detCarga);

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
                BigDecimal sueldoMensualBs = sueldoMensualUsd;
                BigDecimal sueldoQuincenalBs = sueldoQuincenalUsd;

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
                BigDecimal montoCajaAhorroBs = BigDecimal.ZERO;
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

                    montoCajaAhorroBs = sueldoQuincenalBs.multiply(porcCajaAhorro).setScale(2, RoundingMode.HALF_UP);

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
                        Detalle_nomina detFalta = new Detalle_nomina();
                        detFalta.setTipoDetalle("DESCUENTO_INASISTENCIA");
                        detFalta.setInasistencia(inasistencia);
                        detFalta.setValor(inasistencia.getMontoTotal());
                        detallesTemporales.add(detFalta);
                    }
                    inasistencia.setActivo(false);
                    inasistenciaRepo.save(inasistencia);
                }

                // ==========================================
                // CÁLCULO 5: AJUSTES MANUALES (Positivos y Negativos)
                // ==========================================
                BigDecimal totalAjustesPositivosBs = BigDecimal.ZERO;
                BigDecimal totalAjustesNegativosBs = BigDecimal.ZERO;

                List<Ajuste> ajustesPendientes = ajusteRepository.findByEmpleadoIdAndProcesadoFalse(empleado.getId());

                for (Ajuste ajuste : ajustesPendientes) {
                    // Asumiendo que el monto del ajuste se registró en USD, lo pasamos a Bs
                    BigDecimal montoAjusteBs = BigDecimal.valueOf(ajuste.getMonto());

                    Detalle_nomina detAjuste = new Detalle_nomina();
                    detAjuste.setValor(montoAjusteBs);
                    // Opcional: si tu Detalle_nomina tiene un campo de descripción, puedes ponerle ajuste.getMotivo()

                    if ("POSITIVO".equalsIgnoreCase(ajuste.getTipo())) {
                        totalAjustesPositivosBs = totalAjustesPositivosBs.add(montoAjusteBs);
                        detAjuste.setTipoDetalle("AJUSTE_POSITIVO");
                        System.out.println("-------------ingresos positivos *-------------- " + totalAjustesPositivosBs);
                    } else {
                        totalAjustesNegativosBs = totalAjustesNegativosBs.add(montoAjusteBs);
                        detAjuste.setTipoDetalle("AJUSTE_NEGATIVO");
                        System.out.println("+++++++++++++ ajustes negativos --------------" + totalAjustesNegativosBs);
                    }

                    detallesTemporales.add(detAjuste);

                    // Quemar el ajuste para que no se pague/descuente doble en la siguiente quincena
                    ajuste.setProcesado(true);
                    ajusteRepository.save(ajuste);
                }

                BigDecimal totalDescuentoDeudasBs = BigDecimal.ZERO;
                List<Novedad_descuento> deudas = novedadRepo.findByEmpleadoIdAndProcesadoFalse(empleado.getId());

                for (Novedad_descuento deuda : deudas) {
                    BigDecimal cuota = deuda.getCuota_por_nomina();
                    if (cuota.compareTo(deuda.getSaldo_pendiente()) > 0) {
                        cuota = deuda.getSaldo_pendiente();
                    }
                    Detalle_nomina detDeuda = new Detalle_nomina();
                    detDeuda.setTipoDetalle("DESCUENTO_DEUDA");
                    detDeuda.setNovedadDescuento(deuda);
                    detDeuda.setValor(cuota);
                    detallesTemporales.add(detDeuda);

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
                System.out.println("--------------- ajuste -------" + totalAjustesPositivosBs);
                recibo.setTotal_ingreso(totalIngresoAsignacionesBs.add(totalAjustesPositivosBs));

                recibo.setValor_cestaticket(factorCestaticket);
                recibo.setMonto_cestaticket(montoCestaticketBs);

                recibo.setMontoCajaAhorro(montoCajaAhorroBs);
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

                BigDecimal totalDeduccionesBs = totalDescuentosLegalesBs.add(totalDescuentoDeudasBs).add(totalDescuentoInasistenciaBs).add(totalAjustesNegativosBs).add(montoCajaAhorroBs);                recibo.setTotal_deducciones(totalDeduccionesBs);

                BigDecimal granTotalBs = totalIngresoAsignacionesBs.add(montoCestaticketBs);
                BigDecimal netoAPagarBs = granTotalBs.subtract(totalDeduccionesBs);

                if (netoAPagarBs.compareTo(BigDecimal.ZERO) < 0) netoAPagarBs = BigDecimal.ZERO;

                recibo.setTotal_bs(netoAPagarBs);
                recibo.setNeto_a_pagar(netoAPagarBs.divide(tasaBcv, 2, RoundingMode.HALF_UP));

                nominaRepository.save(recibo);

                for (Detalle_nomina det : detallesTemporales) {
                    det.setNomina(recibo); // Le asignamos el ID de la cabecera recién creada
                    detalleNominaRepo.save(det);
                }

                // REGISTRO EN EL HISTORIAL (LEDGER) DE CAJA AHORRO
                if (montoCajaAhorroBs.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal saldoAnterior = BigDecimal.ZERO;
                    Optional<MovimientoCajaAhorro> ultimoMov = movimientoCajaAhorroRepository.findUltimoMovimientoByEmpleado(empleado.getId());
                    if (ultimoMov.isPresent()) {
                        saldoAnterior = ultimoMov.get().getAcumuladoTotal();
                    }

                    MovimientoCajaAhorro nuevoMovimiento = new MovimientoCajaAhorro();
                    nuevoMovimiento.setEmpleado(empleado);
                    nuevoMovimiento.setTipoMovimiento("APORTE_NÓMINA");
                    nuevoMovimiento.setReferencia("Lote #" + lote.getId() + " - " + lote.getDescripcion());
                    nuevoMovimiento.setAporteEmpleado(montoCajaAhorroBs);
                    nuevoMovimiento.setAporteUniversidad(montoCajaAhorroBs); // La universidad aporta el mismo %

                    BigDecimal nuevoTotal = saldoAnterior.add(montoCajaAhorroBs).add(montoCajaAhorroBs);
                    nuevoMovimiento.setAcumuladoTotal(nuevoTotal);
                    nuevoMovimiento.setFechaOperacion(LocalDateTime.now());

                    movimientoCajaAhorroRepository.save(nuevoMovimiento);
                }

                log.info("-> ✅ Guardado: Empleado ID " + empleado.getId() + " | Neto Bs: " + netoAPagarBs);

            } catch (Exception e) {
                log.error("❌ Error procesando empleado ID: " + empleado.getId(), e);
            }
        }

        log.info("---- CÁLCULO DE NÓMINA FINALIZADO ----");
    }
}