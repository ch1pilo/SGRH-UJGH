package com.example.sigerh_ujgh.dto;

import java.math.BigDecimal;

public class ReciboNominaDTO {

    private Long idNomina;
    private String periodo;
    private BigDecimal tasaBcv;

    // --- DATOS DEL EMPLEADO ---
    private String cedula;
    private String nombreCompleto;
    private String cargoContrato;

    // --- ASIGNACIONES (INGRESOS) ---
    private BigDecimal sueldoBase;
    private BigDecimal horasDiurnas;
    private BigDecimal horasNocturnas;
    private BigDecimal horasFinSemana;
    private BigDecimal bonoCestaticket;
    private BigDecimal totalAsignaciones;

    // --- DEDUCCIONES DE LEY ---
    private BigDecimal montoSSO;
    private BigDecimal montoSPF;
    private BigDecimal montoFAOV;
    private BigDecimal totalDeduccionesLey;

    // --- OTRAS DEDUCCIONES ---
    private BigDecimal inasistencias;
    private BigDecimal otrasDeudas;
    private BigDecimal totalOtrasDeducciones;

    // --- TOTALES FINALES ---
    private BigDecimal totalDeducciones;
    private BigDecimal netoAPagarBs;
    private BigDecimal netoAPagarUsd;

    public Long getIdNomina() {
        return idNomina;
    }

    public void setIdNomina(Long idNomina) {
        this.idNomina = idNomina;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public BigDecimal getTasaBcv() {
        return tasaBcv;
    }

    public void setTasaBcv(BigDecimal tasaBcv) {
        this.tasaBcv = tasaBcv;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getCargoContrato() {
        return cargoContrato;
    }

    public void setCargoContrato(String cargoContrato) {
        this.cargoContrato = cargoContrato;
    }

    public BigDecimal getSueldoBase() {
        return sueldoBase;
    }

    public void setSueldoBase(BigDecimal sueldoBase) {
        this.sueldoBase = sueldoBase;
    }

    public BigDecimal getHorasDiurnas() {
        return horasDiurnas;
    }

    public void setHorasDiurnas(BigDecimal horasDiurnas) {
        this.horasDiurnas = horasDiurnas;
    }

    public BigDecimal getHorasNocturnas() {
        return horasNocturnas;
    }

    public void setHorasNocturnas(BigDecimal horasNocturnas) {
        this.horasNocturnas = horasNocturnas;
    }

    public BigDecimal getHorasFinSemana() {
        return horasFinSemana;
    }

    public void setHorasFinSemana(BigDecimal horasFinSemana) {
        this.horasFinSemana = horasFinSemana;
    }

    public BigDecimal getBonoCestaticket() {
        return bonoCestaticket;
    }

    public void setBonoCestaticket(BigDecimal bonoCestaticket) {
        this.bonoCestaticket = bonoCestaticket;
    }

    public BigDecimal getTotalAsignaciones() {
        return totalAsignaciones;
    }

    public void setTotalAsignaciones(BigDecimal totalAsignaciones) {
        this.totalAsignaciones = totalAsignaciones;
    }

    public BigDecimal getMontoSSO() {
        return montoSSO;
    }

    public void setMontoSSO(BigDecimal montoSSO) {
        this.montoSSO = montoSSO;
    }

    public BigDecimal getMontoSPF() {
        return montoSPF;
    }

    public void setMontoSPF(BigDecimal montoSPF) {
        this.montoSPF = montoSPF;
    }

    public BigDecimal getMontoFAOV() {
        return montoFAOV;
    }

    public void setMontoFAOV(BigDecimal montoFAOV) {
        this.montoFAOV = montoFAOV;
    }

    public BigDecimal getTotalDeduccionesLey() {
        return totalDeduccionesLey;
    }

    public void setTotalDeduccionesLey(BigDecimal totalDeduccionesLey) {
        this.totalDeduccionesLey = totalDeduccionesLey;
    }

    public BigDecimal getInasistencias() {
        return inasistencias;
    }

    public void setInasistencias(BigDecimal inasistencias) {
        this.inasistencias = inasistencias;
    }

    public BigDecimal getOtrasDeudas() {
        return otrasDeudas;
    }

    public void setOtrasDeudas(BigDecimal otrasDeudas) {
        this.otrasDeudas = otrasDeudas;
    }

    public BigDecimal getTotalOtrasDeducciones() {
        return totalOtrasDeducciones;
    }

    public void setTotalOtrasDeducciones(BigDecimal totalOtrasDeducciones) {
        this.totalOtrasDeducciones = totalOtrasDeducciones;
    }

    public BigDecimal getTotalDeducciones() {
        return totalDeducciones;
    }

    public void setTotalDeducciones(BigDecimal totalDeducciones) {
        this.totalDeducciones = totalDeducciones;
    }

    public BigDecimal getNetoAPagarBs() {
        return netoAPagarBs;
    }

    public void setNetoAPagarBs(BigDecimal netoAPagarBs) {
        this.netoAPagarBs = netoAPagarBs;
    }

    public BigDecimal getNetoAPagarUsd() {
        return netoAPagarUsd;
    }

    public void setNetoAPagarUsd(BigDecimal netoAPagarUsd) {
        this.netoAPagarUsd = netoAPagarUsd;
    }
}
