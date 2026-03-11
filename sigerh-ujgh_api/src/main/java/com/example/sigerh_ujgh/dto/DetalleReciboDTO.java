package com.example.sigerh_ujgh.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DetalleReciboDTO {
    private String concepto;  // Ej: "Turno Sabatino", "Falta Injustificada", "Cobro de Préstamo"
    private BigDecimal horas; // Opcional: Ej: "4.0" (Si aplica)
    private BigDecimal monto; // Ej: 150.00

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public BigDecimal getHoras() {
        return horas;
    }

    public void setHoras(BigDecimal horas) {
        this.horas = horas;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
}