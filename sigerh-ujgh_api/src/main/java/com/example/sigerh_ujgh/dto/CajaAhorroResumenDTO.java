package com.example.sigerh_ujgh.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CajaAhorroResumenDTO {
    private Long idEmpleado;
    private String cedula;
    private String nombre;
    private BigDecimal ultimoAporte;
    private BigDecimal acumuladoTotal;
    private Boolean activa;

    public Long getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Long idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getUltimoAporte() {
        return ultimoAporte;
    }

    public void setUltimoAporte(BigDecimal ultimoAporte) {
        this.ultimoAporte = ultimoAporte;
    }

    public BigDecimal getAcumuladoTotal() {
        return acumuladoTotal;
    }

    public void setAcumuladoTotal(BigDecimal acumuladoTotal) {
        this.acumuladoTotal = acumuladoTotal;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }
}