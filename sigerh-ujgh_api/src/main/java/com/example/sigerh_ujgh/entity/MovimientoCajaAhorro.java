package com.example.sigerh_ujgh.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class MovimientoCajaAhorro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_empleado")
    private Empleado empleado;

    private String tipoMovimiento;

    private String referencia;

    private BigDecimal aporteEmpleado;

    private BigDecimal aporteUniversidad;

    private BigDecimal acumuladoTotal;

    private LocalDateTime fechaOperacion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public BigDecimal getAporteEmpleado() {
        return aporteEmpleado;
    }

    public void setAporteEmpleado(BigDecimal aporteEmpleado) {
        this.aporteEmpleado = aporteEmpleado;
    }

    public BigDecimal getAporteUniversidad() {
        return aporteUniversidad;
    }

    public void setAporteUniversidad(BigDecimal aporteUniversidad) {
        this.aporteUniversidad = aporteUniversidad;
    }

    public BigDecimal getAcumuladoTotal() {
        return acumuladoTotal;
    }

    public void setAcumuladoTotal(BigDecimal acumuladoTotal) {
        this.acumuladoTotal = acumuladoTotal;
    }

    public LocalDateTime getFechaOperacion() {
        return fechaOperacion;
    }

    public void setFechaOperacion(LocalDateTime fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }
}