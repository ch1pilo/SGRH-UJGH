package com.example.sigerh_ujgh.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
public class Carga_academica {

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Periodo_academico getPeriodoAcademico() {
        return periodoAcademico;
    }

    public void setPeriodoAcademico(Periodo_academico periodoAcademico) {
        this.periodoAcademico = periodoAcademico;
    }

    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    public Integer getHoras_semanales() {
        return horas_semanales;
    }

    public void setHoras_semanales(Integer horas_semanales) {
        this.horas_semanales = horas_semanales;
    }

    public String getObservacion() {
        return observacion;
    }

    public Contrato getId_contrato() {
        return id_contrato;
    }

    public void setId_contrato(Contrato id_contrato) {
        this.id_contrato = id_contrato;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn( name = "id_empleado", nullable = false)
    private Empleado empleado;

    @ManyToOne
    @JoinColumn(name = "id_periodo_academico", nullable = false)
    private Periodo_academico periodoAcademico;

    @ManyToOne
    @JoinColumn(name = "id_turno", nullable = false)
    private Turno turno;

    @Column
    private Integer horas_semanales;

    @Column
    private String observacion;

    @ManyToOne
    @JoinColumn(name = "id_contrato", nullable = false)
    private Contrato id_contrato;
}
