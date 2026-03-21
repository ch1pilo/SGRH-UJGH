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
    private Detalle_nominaRepository detalleNominaRepository;
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private NominaRepository nominaRepository;

    public void enviarCorreosPorLote(Long idLote) {
        List<Nomina> nominas = nominaRepository.findByLoteNominaId(idLote);

        for (Nomina recibo : nominas) {
            String correoEmpleado = recibo.getEmpleado().getPersona().getCorreo();

            if (correoEmpleado != null && !correoEmpleado.trim().isEmpty()) {
                try {
                    String nombre = recibo.getEmpleado().getPersona().getNombre();
                    String apellido = recibo.getEmpleado().getPersona().getApellido();
                    String cedula = recibo.getEmpleado().getPersona().getCedula();
                    String periodo = recibo.getLoteNomina().getDescripcion();

                    List<Detalle_nomina> detalles = detalleNominaRepository.findByNominaId(recibo.getId());

                    StringBuilder cuerpo = new StringBuilder();

                    // --- ENCABEZADO FORMAL ---
                    cuerpo.append("UNIVERSIDAD DR. JOSÉ GREGORIO HERNÁNDEZ\n");
                    cuerpo.append("DIRECCIÓN DE TALENTO HUMANO ACADÉMICO\n");
                    cuerpo.append("RECIBO DE PAGO DE NÓMINA\n");
                    cuerpo.append("=======================================================================\n\n");

                    cuerpo.append(String.format("Empleado: %s %s\n", nombre, apellido));
                    cuerpo.append(String.format("No. Cédula: %s\n", cedula));
                    cuerpo.append(String.format("Correspondiente al período: %s\n", periodo));
                    // Aquí puedes agregar cargo, fecha de ingreso, etc., si los tienes en la BD.
                    cuerpo.append("=======================================================================\n\n");

                    // --- ENCABEZADO DE LA TABLA ---
                    // Alineamos: Concepto (35 chars), Referencia (10 chars), Asignaciones (12 chars), Deducciones (12 chars)
                    cuerpo.append(String.format("%-35s %10s %12s %12s\n", "Concepto", "Referencia", "Asignaciones", "Deducciones"));
                    cuerpo.append("-----------------------------------------------------------------------\n");

                    // --- CONCEPTOS DE PAGO (ASIGNACIONES FIJAS) ---
                    cuerpo.append(String.format("%-35s %10s %12.2f %12s\n",
                            "SUELDO BASE", "1.00", recibo.getSueldo_base(), ""));

                    if (recibo.getMonto_cestaticket() != null && recibo.getMonto_cestaticket().compareTo(BigDecimal.ZERO) > 0) {
                        cuerpo.append(String.format("%-35s %10s %12.2f %12s\n",
                                "CESTA TICKET", "1.00", recibo.getMonto_cestaticket(), ""));
                    }

                    // --- DETALLES DINÁMICOS (CARGA ACADÉMICA / INASISTENCIAS) ---
                    for (Detalle_nomina d : detalles) {
                        // NOTA: Si tu entidad Detalle_nomina tiene un campo de "cantidad" u "horas",
                        // reemplaza el "1.00" quemado por String.format("%.2f", d.getCantidad())
                        String referenciaStr = "1.00";

                        if ("ASIGNACION_CARGA".equals(d.getTipoDetalle()) || "AJUSTE_POSITIVO".equals(d.getTipoDetalle())) {
                            String concepto = d.getTipoDetalle().equals("ASIGNACION_CARGA") ? "HORAS ACADÉMICAS DIURNAS" : "BONO / AJUSTE POSITIVO";
                            cuerpo.append(String.format("%-35s %10s %12.2f %12s\n",
                                    concepto, referenciaStr, d.getValor(), ""));
                        }
                        else if ("INASISTENCIA".equals(d.getTipoDetalle()) || "AJUSTE_NEGATIVO".equals(d.getTipoDetalle())) {
                            String concepto = d.getTipoDetalle().equals("INASISTENCIA") ? "DESCUENTO INASISTENCIA" : "AJUSTE NEGATIVO";
                            // Se imprime en la columna de deducciones
                            cuerpo.append(String.format("%-35s %10s %12s %12.2f\n",
                                    concepto, referenciaStr, "", d.getValor()));
                        }
                    }

                    // --- DEDUCCIONES DE LEY ---
                    cuerpo.append(String.format("%-35s %10s %12s %12.2f\n", "S.S.O. (4%)", "", "", recibo.getMonto_sso()));
                    cuerpo.append(String.format("%-35s %10s %12s %12.2f\n", "S.P.F. (0.5%)", "", "", recibo.getMonto_spf()));
                    cuerpo.append(String.format("%-35s %10s %12s %12.2f\n", "F.A.O.V. (1%)", "", "", recibo.getMonto_faov()));

                    cuerpo.append("-----------------------------------------------------------------------\n");

                    // --- TOTALES ---
                    cuerpo.append(String.format("%-46s %12.2f %12.2f\n",
                            "Total Asignaciones y Deducciones", recibo.getTotal_ingreso(), recibo.getTotal_deducciones()));

                    cuerpo.append(String.format("%-46s %12.2f\n",
                            "Neto a Cobrar", recibo.getTotal_bs()));

                    cuerpo.append("=======================================================================\n\n");
                    cuerpo.append("Este es un comprobante digital emitido por el Sistema SIGERH-THACA-UJGH.\n");

                    // --- ENVIAR EL CORREO ---
                    SimpleMailMessage mensaje = new SimpleMailMessage();
                    mensaje.setFrom("tu_correo@gmail.com");
                    mensaje.setTo(correoEmpleado);
                    mensaje.setSubject("Recibo de Pago - " + periodo);
                    mensaje.setText(cuerpo.toString());

                    mailSender.send(mensaje);
                    System.out.println("✅ Correo enviado a: " + correoEmpleado);

                } catch (Exception e) {
                    System.err.println("❌ Error al enviar a: " + correoEmpleado);
                    e.printStackTrace();
                }
            }
        }
    }
}