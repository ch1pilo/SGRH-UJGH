package com.example.sigerh_ujgh.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

@Data
public class ReciboNominaDTO {
    // --- DATOS DEL EMPLEADO Y LOTE ---
    private Long idNomina;
    private String nombreCompleto;
    private String cedula;
    private String cargo;
    private String periodoPago; // Ej: "1ra Quincena Marzo 2026"

    // --- TOTALES (Para la parte de abajo del recibo) ---
    private BigDecimal sueldoBaseBs;
    private BigDecimal totalAsignaciones;
    private BigDecimal totalDeducciones;
    private BigDecimal netoAPagar;

    // --- LAS LISTAS DINÁMICAS ---
    private List<DetalleReciboDTO> asignaciones = new ArrayList<>();
    private List<DetalleReciboDTO> deducciones = new ArrayList<>();

    public Long getIdNomina() {
        return idNomina;
    }

    public void setIdNomina(Long idNomina) {
        this.idNomina = idNomina;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getPeriodoPago() {
        return periodoPago;
    }

    public void setPeriodoPago(String periodoPago) {
        this.periodoPago = periodoPago;
    }

    public BigDecimal getSueldoBaseBs() {
        return sueldoBaseBs;
    }

    public void setSueldoBaseBs(BigDecimal sueldoBaseBs) {
        this.sueldoBaseBs = sueldoBaseBs;
    }

    public BigDecimal getTotalAsignaciones() {
        return totalAsignaciones;
    }

    public void setTotalAsignaciones(BigDecimal totalAsignaciones) {
        this.totalAsignaciones = totalAsignaciones;
    }

    public BigDecimal getTotalDeducciones() {
        return totalDeducciones;
    }

    public void setTotalDeducciones(BigDecimal totalDeducciones) {
        this.totalDeducciones = totalDeducciones;
    }

    public BigDecimal getNetoAPagar() {
        return netoAPagar;
    }

    public void setNetoAPagar(BigDecimal netoAPagar) {
        this.netoAPagar = netoAPagar;
    }

    public List<DetalleReciboDTO> getAsignaciones() {
        return asignaciones;
    }

    public void setAsignaciones(List<DetalleReciboDTO> asignaciones) {
        this.asignaciones = asignaciones;
    }

    public List<DetalleReciboDTO> getDeducciones() {
        return deducciones;
    }

    public void setDeducciones(List<DetalleReciboDTO> deducciones) {
        this.deducciones = deducciones;
    }
}