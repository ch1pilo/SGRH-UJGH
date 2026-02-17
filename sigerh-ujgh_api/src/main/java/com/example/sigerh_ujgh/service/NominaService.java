package com.example.sigerh_ujgh.service;

import com.example.sigerh_ujgh.entity.*;
import com.example.sigerh_ujgh.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@Slf4j
public class NominaService {
    @Autowired
    private NominaRepository nominaRepository;
    @Autowired
    private LoteNominaRepository loteNominaRepository;
    @Autowired
    private Carga_academicaRepository cargaRepository;
    @Autowired
    private Novedad_descuentoRepository novedad_descuentoRepository;
    @Autowired
    private ContratoRepository contratoRepository;
    @Autowired
    private Configuracion_globalRepository configRepository;
    @Autowired
    private EmpleadoRepository empleadoRepository;
    @Autowired
    private InasistenciaRepository inasistenciaRepo;

    public List<Nomina> listarPorLote(Long idLoteNomina) {
        return nominaRepository.findByLoteNominaId(idLoteNomina);
    }

    @Transactional
    public void calcularNominaMasiva(Long idLoteNomina) {

        System.out.println("---- INICIO PROCESO DE NÓMINA ----");

        // 1. VALIDACIONES
        LoteNomina lote = loteNominaRepository.findById(idLoteNomina)
                .orElseThrow(() -> new RuntimeException("Lote no encontrado"));

        if ("CERRADO".equalsIgnoreCase(lote.getEstatus())) {
            throw new RuntimeException("Lote cerrado. No se puede recalcular.");
        }

        boolean esRecalculo = nominaRepository.existsByLoteNominaId(idLoteNomina);

        // 2. LIMPIEZA
        nominaRepository.deleteByLoteNominaId(idLoteNomina);
        nominaRepository.flush();

        // 3. CONFIGURACIONES
        BigDecimal tasaBcv = obtenerConfig("tasa_bcv", BigDecimal.ZERO);
        BigDecimal factorNocturno = obtenerConfig("factor_turno_nocturno", BigDecimal.ONE);
        BigDecimal factorFinde = obtenerConfig("factor_fin_de_semana", BigDecimal.ONE);

        BigDecimal porcSSO = obtenerConfig("porcentaje_sso", BigDecimal.ZERO);
        BigDecimal porcSPF = obtenerConfig("porcentaje_spf", BigDecimal.ZERO);
        BigDecimal porcFAOV = obtenerConfig("porcentaje_faov", BigDecimal.ZERO);

        BigDecimal valorCestaMensual = obtenerConfig("valor_cestaticket", BigDecimal.ZERO);
        BigDecimal cestaticketQuincenal = valorCestaMensual.divide(new BigDecimal("2"), 2, RoundingMode.HALF_UP);

        // 4. PROCESAR EMPLEADOS
        List<Empleado> empleados = empleadoRepository.findByEstatus("1");

        for (Empleado empleado : empleados) {
            try {
                // ============================================================
                // A. INGRESOS (Sueldos + Cargas)
                // ============================================================
                BigDecimal totalIngresoDolares = BigDecimal.ZERO;
                BigDecimal totalSueldoFijo = BigDecimal.ZERO;
                BigDecimal totalPagoDiurno = BigDecimal.ZERO;
                BigDecimal totalPagoNocturno = BigDecimal.ZERO;
                BigDecimal totalPagoFinesSemana = BigDecimal.ZERO;

                // Variable para tener una referencia del valor de hora promedio del empleado
                // (Nos servirá para calcular el costo de la inasistencia si no tiene monto fijo)
                BigDecimal valorHoraReferencia = BigDecimal.ZERO;

                // A1. Contratos
                List<Contrato> contratos = contratoRepository.findByEmpleadoIdAndActivoTrue(empleado.getId());
                for (Contrato c : contratos) {
                    if (c.getIdTabuladorSalarial() != null) {
                        BigDecimal sueldoMes = BigDecimal.valueOf(c.getIdTabuladorSalarial().getSueldo_base());
                        totalSueldoFijo = totalSueldoFijo.add(sueldoMes.divide(new BigDecimal(2), 2, RoundingMode.HALF_UP));

                        // Guardamos el valor hora de su contrato principal por si acaso
                        if(valorHoraReferencia.equals(BigDecimal.ZERO)) {
                            valorHoraReferencia = BigDecimal.valueOf(c.getIdTabuladorSalarial().getValor_hora());
                        }
                    }
                }
                totalIngresoDolares = totalIngresoDolares.add(totalSueldoFijo);

                // A2. Cargas Académicas
                List<Carga_academica> cargas = cargaRepository.findByEmpleadoId(empleado.getId());
                for (Carga_academica carga : cargas) {
                    if (carga.getId_contrato() == null || carga.getId_contrato().getIdTabuladorSalarial() == null) continue;

                    BigDecimal valorHora = BigDecimal.valueOf(carga.getId_contrato().getIdTabuladorSalarial().getValor_hora());
                    BigDecimal horasQuin = new BigDecimal(carga.getHoras_semanales()).multiply(new BigDecimal(2));
                    BigDecimal base = horasQuin.multiply(valorHora);

                    // Actualizamos referencia si es docente
                    valorHoraReferencia = valorHora;

                    if (carga.getTurno() != null) {
                        if (Boolean.TRUE.equals(carga.getTurno().isNocturno())) {
                            BigDecimal p = base.multiply(factorNocturno);
                            totalPagoNocturno = totalPagoNocturno.add(p);
                            totalIngresoDolares = totalIngresoDolares.add(p);
                        } else if (Boolean.TRUE.equals(carga.getTurno().isFin_de_semana())) {
                            BigDecimal p = base.multiply(factorFinde);
                            totalPagoFinesSemana = totalPagoFinesSemana.add(p);
                            totalIngresoDolares = totalIngresoDolares.add(p);
                        } else {
                            totalPagoDiurno = totalPagoDiurno.add(base);
                            totalIngresoDolares = totalIngresoDolares.add(base);
                        }
                    }
                }

                if (totalIngresoDolares.compareTo(BigDecimal.ZERO) == 0 && cestaticketQuincenal.compareTo(BigDecimal.ZERO) == 0) continue;

                // ============================================================
                // B. DEDUCCIONES LEY (Sobre Cestaticket)
                // ============================================================
                BigDecimal montoSSO = cestaticketQuincenal.multiply(porcSSO).setScale(2, RoundingMode.HALF_UP);
                BigDecimal montoSPF = cestaticketQuincenal.multiply(porcSPF).setScale(2, RoundingMode.HALF_UP);
                BigDecimal montoFAOV = cestaticketQuincenal.multiply(porcFAOV).setScale(2, RoundingMode.HALF_UP);
                BigDecimal totalLey = montoSSO.add(montoSPF).add(montoFAOV);

                BigDecimal cestaticketNeto = cestaticketQuincenal.subtract(totalLey);
                if (cestaticketNeto.compareTo(BigDecimal.ZERO) < 0) cestaticketNeto = BigDecimal.ZERO;

                // ============================================================
                // C. PRÉSTAMOS (Logica Anti-Recálculo)
                // ============================================================
                BigDecimal totalPrestamos = BigDecimal.ZERO;
                List<Novedad_descuento> novedades = novedad_descuentoRepository.findByEmpleadoIdAndProcesadoFalse(empleado.getId());

                for (Novedad_descuento nov : novedades) {
                    BigDecimal saldo = (nov.getSaldo_pendiente() != null) ? nov.getSaldo_pendiente() : BigDecimal.ZERO;
                    BigDecimal desc = BigDecimal.ZERO;

                    if (saldo.compareTo(BigDecimal.ZERO) > 0) {
                        BigDecimal cuota = (nov.getCuota_por_nomina() != null) ? nov.getCuota_por_nomina() : saldo;
                        desc = (saldo.compareTo(cuota) < 0) ? saldo : cuota;
                    } else if (nov.getMonto_total_deuda() != null) {
                        desc = nov.getMonto_total_deuda();
                    }

                    // Solo actualizamos saldo en BD si NO es recálculo
                    if (!esRecalculo) {
                        nov.setSaldo_pendiente(saldo.subtract(desc));
                        if (nov.getSaldo_pendiente().compareTo(BigDecimal.ZERO) == 0) nov.setProcesado(true);
                        novedad_descuentoRepository.save(nov);
                    }
                    totalPrestamos = totalPrestamos.add(desc);
                }

                // ============================================================
                // D. INASISTENCIAS (¡NUEVO!)
                // ============================================================
                BigDecimal totalInasistencias = BigDecimal.ZERO;

                // Buscamos las faltas PENDIENTES (activo = true)
                List<Inacistencia> faltas = inasistenciaRepo.findByEmpleadoAndActivoTrue(empleado);

                for (Inacistencia falta : faltas) {
                    BigDecimal costoFalta = BigDecimal.ZERO;

                    // Opción 1: Si ya guardamos el monto al registrarla, lo usamos
                    if (falta.getMontoTotal() != null && falta.getMontoTotal().compareTo(BigDecimal.ZERO) > 0) {
                        costoFalta = falta.getMontoTotal();
                    }
                    // Opción 2: Si no hay monto guardado, calculamos (Horas * ValorHoraActual)
                    else {
                        costoFalta = valorHoraReferencia.multiply(new BigDecimal(falta.getHoras()));
                    }

                    totalInasistencias = totalInasistencias.add(costoFalta);

                    // OJO: Aquí NO cambiamos 'activo' a false todavía.
                    // Lo ideal es hacerlo al CERRAR la nómina para permitir recálculos.
                    // Pero si quieres que aparezca descontado, lo dejamos así sumado.
                }


                // ============================================================
                // E. CÁLCULOS FINALES
                // ============================================================

                // Total Descuentos al Sueldo = Préstamos + Inasistencias
                BigDecimal totalDeduccionesSueldo = totalPrestamos.add(totalInasistencias);

                // Sueldo Líquido
                BigDecimal sueldoLiquido = totalIngresoDolares.subtract(totalDeduccionesSueldo);
                if (sueldoLiquido.compareTo(BigDecimal.ZERO) < 0) sueldoLiquido = BigDecimal.ZERO;

                // Neto a Pagar
                BigDecimal netoUSD = sueldoLiquido.add(cestaticketNeto);
                BigDecimal totalBS = netoUSD.multiply(tasaBcv).setScale(2, RoundingMode.HALF_UP);

                // ============================================================
                // F. GUARDAR RECIBO
                // ============================================================
                Nomina recibo = new Nomina();
                recibo.setLoteNomina(lote);
                recibo.setEmpleado(empleado);

                // Ingresos
                recibo.setSueldo_base(totalSueldoFijo);
                recibo.setTotal_ingreso(totalIngresoDolares);
                // ... (Tus setters de horas diurnas/nocturnas van aqui)

                // Deducciones Ley
                recibo.setMonto_sso(montoSSO);
                recibo.setMonto_spe(montoSPF);
                recibo.setMonto_faov(montoFAOV);

                // --- NUEVO: GUARDAR INASISTENCIAS ---
                recibo.setMonto_descuento_hora(totalInasistencias); // <--- TU COLUMNA NUEVA

                // Préstamos
                recibo.setOtros_descuento(totalPrestamos);

                // Total Deducciones (Informativo: Ley + Prestamos + Faltas)
                BigDecimal granTotalDeducciones = totalLey.add(totalPrestamos).add(totalInasistencias);
                recibo.setTotal_deducciones(granTotalDeducciones);

                // Netos
                recibo.setMonto_cestaticket(cestaticketNeto);
                recibo.setNeto_a_pagar(netoUSD);
                recibo.setTotal_bs(totalBS);

                nominaRepository.save(recibo);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Método Auxiliar seguro
    private BigDecimal obtenerConfig(String clave, BigDecimal valorDefecto) {
        return configRepository.findByClave(clave)
                .map(Configuracion_global::getValor)
                .orElse(valorDefecto);
    }
}