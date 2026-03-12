package com.example.sigerh_ujgh.service;

import com.example.sigerh_ujgh.entity.Detalle_nomina;
import com.example.sigerh_ujgh.entity.Nomina;
import com.example.sigerh_ujgh.repository.Detalle_nominaRepository;
import com.example.sigerh_ujgh.repository.NominaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CorreoService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private NominaRepository nominaRepository;

    @Autowired
    private Detalle_nominaRepository detalleNominaRepository; // <-- Asegúrate de tener este import
    public void enviarCorreosPorLote(Long idLote) {
        List<Nomina> nominas = nominaRepository.findByLoteNominaId(idLote);

        for (Nomina recibo : nominas) {
            String correoEmpleado = recibo.getEmpleado().getPersona().getCorreo();

            if (correoEmpleado != null && !correoEmpleado.trim().isEmpty()) {
                try {
                    String nombre = recibo.getEmpleado().getPersona().getNombre();
                    String cedula = recibo.getEmpleado().getPersona().getCedula();
                    String periodo = recibo.getLoteNomina().getDescripcion();

                    // Buscamos los detalles (turnos, ajustes, inasistencias) de este recibo en particular
                    List<Detalle_nomina> detalles = detalleNominaRepository.findByNominaId(recibo.getId());

                    // --- ARMAMOS EL RECIBO DETALLADO ---
                    StringBuilder cuerpo = new StringBuilder();
                    cuerpo.append("Hola ").append(nombre).append(",\n\n");
                    cuerpo.append("Adjunto el detalle de tu recibo de pago correspondiente al periodo '").append(periodo).append("':\n\n");

                    cuerpo.append("===================================================\n");
                    cuerpo.append("                 RECIBO DE NÓMINA\n");
                    cuerpo.append("===================================================\n");
                    cuerpo.append("Empleado: ").append(nombre).append("\n");
                    cuerpo.append("Cédula:   ").append(cedula).append("\n");
                    cuerpo.append("---------------------------------------------------\n\n");

                    // --- SECCIÓN: INGRESOS ---
                    cuerpo.append("✅ INGRESOS (ASIGNACIONES):\n");
                    cuerpo.append(String.format("- Sueldo Base: Bs. %.2f\n", recibo.getSueldo_base()));
                    BigDecimal valor = BigDecimal.valueOf(0);
                    if (recibo.getMonto_cestaticket() != null && recibo.getMonto_cestaticket().compareTo(BigDecimal.ZERO) < 0 ) {
                        cuerpo.append(String.format("- Cesta Ticket: Bs. %.2f\n", recibo.getMonto_cestaticket()));
                    }

                    // Agregamos los detalles dinámicos positivos (Cargas, Turnos, Ajustes)
                    for (Detalle_nomina d : detalles) {
                        if ("ASIGNACION_CARGA".equals(d.getTipoDetalle()) || "AJUSTE_POSITIVO".equals(d.getTipoDetalle())) {
                            String concepto = (d.getTipoDetalle().equals("ASIGNACION_CARGA")) ? "Asignación por Carga Académica" : "Ajuste / Bono Extra";
                            cuerpo.append(String.format("- %s: Bs. %.2f\n", concepto, d.getValor()));
                        }
                    }
                    cuerpo.append(String.format("\nTOTAL INGRESOS: Bs. %.2f\n", recibo.getTotal_ingreso()));
                    cuerpo.append("---------------------------------------------------\n\n");

                    // --- SECCIÓN: EGRESOS ---
                    cuerpo.append("🔻 EGRESOS (DEDUCCIONES DE LEY Y OTROS):\n");
                    cuerpo.append(String.format("- S.S.O. (4%%): Bs. %.2f\n", recibo.getMonto_sso()));
                    cuerpo.append(String.format("- S.P.F. (0.5%%): Bs. %.2f\n", recibo.getMonto_spf()));
                    cuerpo.append(String.format("- F.A.O.V. (1%%): Bs. %.2f\n", recibo.getMonto_faov()));

                    // Agregamos los detalles dinámicos negativos (Inasistencias, Préstamos)
                    for (Detalle_nomina d : detalles) {
                        if ("INASISTENCIA".equals(d.getTipoDetalle()) || "AJUSTE_NEGATIVO".equals(d.getTipoDetalle())) {
                            String concepto = (d.getTipoDetalle().equals("INASISTENCIA")) ? "Descuento por Inasistencia" : "Descuento / Ajuste Negativo";
                            cuerpo.append(String.format("- %s: Bs. %.2f\n", concepto, d.getValor()));
                        }
                    }
                    cuerpo.append(String.format("\nTOTAL DEDUCCIONES: Bs. %.2f\n", recibo.getTotal_deducciones()));
                    cuerpo.append("---------------------------------------------------\n");

                    // --- TOTAL NETO ---
                    cuerpo.append(String.format("💰 NETO A COBRAR: Bs. %.2f\n", recibo.getTotal_bs()));
                    cuerpo.append("===================================================\n\n");
                    cuerpo.append("Atentamente,\nDepartamento de Talento Humanos Academico\nSistema SIGERH-THACA-UJGH.");

                    // --- ENVIAR EL CORREO ---
                    SimpleMailMessage mensaje = new SimpleMailMessage();
                    mensaje.setFrom("tu_correo@gmail.com"); // ¡No olvides tu correo aquí!
                    mensaje.setTo(correoEmpleado);
                    mensaje.setSubject("Recibo Detallado de Nómina - " + periodo);
                    mensaje.setText(cuerpo.toString());

                    mailSender.send(mensaje);
                    System.out.println("✅ Correo detallado enviado a: " + correoEmpleado);

                } catch (Exception e) {
                    System.err.println("❌ Error al enviar a: " + correoEmpleado);
                    e.printStackTrace();
                }
            }
        }
    }
}