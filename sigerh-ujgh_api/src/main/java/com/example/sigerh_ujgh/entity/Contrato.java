package com.example.sigerh_ujgh.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class Contrato {

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

    public Facultad getFacultad() {
        return facultad;
    }

    public void setFacultad(Facultad facultad) {
        this.facultad = facultad;
    }

    public Tipo_contrato getIdTipoContrato() {
        return idTipoContrato;
    }

    public void setIdTipoContrato(Tipo_contrato idTipoContrato) {
        this.idTipoContrato = idTipoContrato;
    }

    public Tabulador_salarial getIdTabuladorSalarial() {
        return idTabuladorSalarial;
    }

    public void setIdTabuladorSalarial(Tabulador_salarial idTabuladorSalarial) {
        this.idTabuladorSalarial = idTabuladorSalarial;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getArchivoDigitalUrl() {
        return archivoDigitalUrl;
    }

    public void setArchivoDigitalUrl(String archivoDigitalUrl) {
        this.archivoDigitalUrl = archivoDigitalUrl;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_empleado")
    private Empleado empleado;

    @ManyToOne
    @JoinColumn(name = "id_facultad")
    private Facultad facultad;

    @ManyToOne
    @JoinColumn(name = "id_tipo_contrato" )
    private Tipo_contrato idTipoContrato;



    @ManyToOne
    @JoinColumn(name = "id_tabulador_salarial")
    private Tabulador_salarial idTabuladorSalarial;
    // ----------------------
    @Column
    private LocalDate fechaInicio;
    @Column
    private LocalDate fechaFin;
    @Column
    private String archivoDigitalUrl;
    @Column
    private Boolean activo = true;
}