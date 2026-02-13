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

    public List<Nomina> listarPorLote(Long idLoteNomina) {
        return nominaRepository.findByLoteNominaId(idLoteNomina);
    }

    @Transactional
    public void calcularNominaMasiva(Long idLoteNomina) {

        // 1. BUSCAR Y VALIDAR EL LOTE
        LoteNomina lote = loteNominaRepository.findById(idLoteNomina)
                .orElseThrow(() -> new RuntimeException("Lote de Nómina no encontrado"));

        if ("CERRADO".equalsIgnoreCase(lote.getEstatus())) {
            throw new RuntimeException("Este lote ya está cerrado y pagado. No se puede recalcular.");
        }

        // 2. LIMPIEZA PREVIA (Para evitar duplicados si recalculas)
        nominaRepository.deleteByLoteNominaId(idLoteNomina);

        // 3. OBTENER CONFIGURACIONES GLOBALES
        BigDecimal tasaBcv = obtenerConfig("tasa_bcv", BigDecimal.ZERO);
        BigDecimal factorNocturno = obtenerConfig("factor_turno_nocturno", new BigDecimal("1.00"));
        BigDecimal factorFinDeSemana = obtenerConfig("factor_fin_de_semana", new BigDecimal("1.00"));

        // Impuestos
        BigDecimal porcSSO = obtenerConfig("porcentaje_sso", BigDecimal.ZERO);
        BigDecimal porcSPF = obtenerConfig("porcentaje_spf", BigDecimal.ZERO);
        BigDecimal porcFAOV = obtenerConfig("porcentaje_faov", BigDecimal.ZERO);

        // Cestaticket (Se divide entre 2 para pago quincenal)
        BigDecimal valorCestaticketMensual = obtenerConfig("valor_cestaticket", BigDecimal.ZERO);
        BigDecimal cestaticketQuincenal = valorCestaticketMensual.divide(new BigDecimal("2"), 2, RoundingMode.HALF_UP);


        // 4. BUSCAR EMPLEADOS ACTIVOS Y CALCULAR
        List<Empleado> empleados = empleadoRepository.findByEstatus("ACTIVO");

        for (Empleado empleado : empleados) {

            // --- INICIALIZAR ACUMULADORES POR EMPLEADO ---
            BigDecimal totalIngresoDolares = BigDecimal.ZERO;

            BigDecimal totalSueldoFijo = BigDecimal.ZERO; // Administrativos
            BigDecimal totalPagoDiurno = BigDecimal.ZERO; // Docentes
            BigDecimal totalPagoNocturno = BigDecimal.ZERO; // Docentes
            BigDecimal totalPagoFinesSemana = BigDecimal.ZERO; // Docentes

            // ---------------------------------------------------
            // A. SUELDO FIJO (CONTRATOS ADMINISTRATIVOS)
            // ---------------------------------------------------
            List<Contrato> contratos = contratoRepository.findByEmpleadoIdAndActivoTrue(empleado.getId());

            for (Contrato contrato : contratos) {
                Tabulador_salarial tab = contrato.getIdTabuladorSalarial();
                if (tab != null && tab.getSueldo_base() > 0) {
                    BigDecimal sueldoMensual = BigDecimal.valueOf(tab.getSueldo_base());
                    // Pago Quincenal = Mensual / 2
                    BigDecimal pagoQuincena = sueldoMensual.divide(new BigDecimal(2), 2, RoundingMode.HALF_UP);

                    totalSueldoFijo = totalSueldoFijo.add(pagoQuincena);
                }
            }
            // Sumamos al global
            totalIngresoDolares = totalIngresoDolares.add(totalSueldoFijo);


            // ---------------------------------------------------
            // B. SUELDO VARIABLE (CARGAS ACADÉMICAS - DOCENTES)
            // ---------------------------------------------------
            List<Carga_academica> cargas = cargaRepository.findByEmpleadoId(empleado.getId());

            for (Carga_academica carga : cargas) {
                // Validación de seguridad
                if (carga.getId_contrato() == null || carga.getId_contrato().getIdTabuladorSalarial() == null) {
                    continue;
                }

                // 1. Datos base
                BigDecimal valorHora = BigDecimal.valueOf(carga.getId_contrato().getIdTabuladorSalarial().getValor_hora());
                BigDecimal horasQuincenales = new BigDecimal(carga.getHoras_semanales()).multiply(new BigDecimal(2));

                // 2. Calculo base
                BigDecimal pagoBaseMateria = horasQuincenales.multiply(valorHora);

                // 3. Aplicar Factores según Turno
                if (carga.getTurno() != null) {

                    if (Boolean.TRUE.equals(carga.getTurno().isNocturno())) {
                        // NOCTURNO
                        BigDecimal pagoNocturno = pagoBaseMateria.multiply(factorNocturno);
                        totalPagoNocturno = totalPagoNocturno.add(pagoNocturno);
                        totalIngresoDolares = totalIngresoDolares.add(pagoNocturno);

                    } else if (Boolean.TRUE.equals(carga.getTurno().isFin_de_semana())) {
                        // FIN DE SEMANA
                        BigDecimal pagoFinde = pagoBaseMateria.multiply(factorFinDeSemana);
                        totalPagoFinesSemana = totalPagoFinesSemana.add(pagoFinde);
                        totalIngresoDolares = totalIngresoDolares.add(pagoFinde);

                    } else {
                        // DIURNO / NORMAL
                        totalPagoDiurno = totalPagoDiurno.add(pagoBaseMateria);
                        totalIngresoDolares = totalIngresoDolares.add(pagoBaseMateria);
                    }
                }
            }

            // SI NO GANÓ NADA, SALTAMOS AL SIGUIENTE EMPLEADO
            if (totalIngresoDolares.compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }

            // ---------------------------------------------------
            // C. DEDUCCIONES DE LEY
            // ---------------------------------------------------
            BigDecimal montoSSO = totalIngresoDolares.multiply(porcSSO);
            BigDecimal montoSPF = totalIngresoDolares.multiply(porcSPF);
            BigDecimal montoFAOV = totalIngresoDolares.multiply(porcFAOV);


            // ---------------------------------------------------
            // D. NOVEDADES (PRÉSTAMOS Y DESCUENTOS EXTRAS)
            // ---------------------------------------------------
            BigDecimal totalOtrosDescuentos = BigDecimal.ZERO;
            List<Novedad_descuento> novedades = novedad_descuentoRepository.findByEmpleadoIdAndProcesadoFalse(empleado.getId());

            for (Novedad_descuento nov : novedades) {
                BigDecimal montoADescontar = BigDecimal.ZERO;

                if (nov.getSaldo_pendiente() != null && nov.getSaldo_pendiente().compareTo(BigDecimal.ZERO) > 0) {
                    // CASO 1: Préstamo por cuotas
                    BigDecimal cuota = (nov.getCuota_por_nomina() != null) ? nov.getCuota_por_nomina() : nov.getSaldo_pendiente();
                    BigDecimal saldo = nov.getSaldo_pendiente();

                    // Si el saldo es menor que la cuota, cobramos solo el saldo restante
                    montoADescontar = (saldo.compareTo(cuota) < 0) ? saldo : cuota;

                    // Actualizamos saldo
                    nov.setSaldo_pendiente(saldo.subtract(montoADescontar));

                    // Si ya pagó todo, marcamos como procesado
                    if (nov.getSaldo_pendiente().compareTo(BigDecimal.ZERO) == 0) {
                        nov.setProcesado(true);
                    }

                } else if (nov.getMonto_total_deuda() != null) {
                    // CASO 2: Cobro único
                    montoADescontar = nov.getMonto_total_deuda();
                    nov.setProcesado(true); // Se marca procesado de una vez
                }

                // Guardamos el estado actualizado de la novedad
                novedad_descuentoRepository.save(nov);
                totalOtrosDescuentos = totalOtrosDescuentos.add(montoADescontar);
            }


            // ---------------------------------------------------
            // E. CÁLCULO FINAL (NETO)
            // ---------------------------------------------------
            BigDecimal totalDeducciones = montoSSO.add(montoSPF).add(montoFAOV).add(totalOtrosDescuentos);

            // Fórmula: (Ingresos - Deducciones) + Cestaticket
            // Nota: Cestaticket no suele ser base para impuestos, por eso se suma al final.
            BigDecimal netoUSD = totalIngresoDolares.subtract(totalDeducciones).add(cestaticketQuincenal);

            // Conversión a Bs
            BigDecimal totalBS = netoUSD.multiply(tasaBcv).setScale(2, RoundingMode.HALF_UP);


            // ---------------------------------------------------
            // F. GUARDAR RECIBO
            // ---------------------------------------------------
            Nomina recibo = new Nomina();
            recibo.setLoteNomina(lote);
            recibo.setEmpleado(empleado);

            // Ingresos Detallados
            recibo.setSueldo_base(totalSueldoFijo);
            recibo.setHora_diurna(totalPagoDiurno);
            recibo.setHora_nocturna(totalPagoNocturno);
            recibo.setHora_fin(totalPagoFinesSemana);

            // Deducciones Detalladas
            recibo.setMonto_sso(montoSSO);
            recibo.setMonto_spe(montoSPF);
            recibo.setMonto_faov(montoFAOV);
            recibo.setOtros_descuento(totalOtrosDescuentos);

            // Totales
            recibo.setTotal_deducciones(totalDeducciones);
            recibo.setTotal_ingreso(totalIngresoDolares);
            recibo.setMonto_cestaticket(cestaticketQuincenal);

            recibo.setNeto_a_pagar(netoUSD);
            recibo.setTotal_bs(totalBS);

            nominaRepository.save(recibo);

            System.out.println("Nómina generada para: " + empleado.getId() + " - Neto: " + netoUSD);
        }
    }

    // Método Auxiliar seguro
    private BigDecimal obtenerConfig(String clave, BigDecimal valorDefecto) {
        return configRepository.findByClave(clave)
                .map(Configuracion_global::getValor)
                .orElse(valorDefecto);
    }
}