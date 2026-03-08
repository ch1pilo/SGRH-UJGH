package com.example.sigerh_ujgh.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "lote_nomina")
public class LoteNomina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descripcion; // Ej: "Primera Quincena Enero 2026"

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;


    @Column(name= "tasa_bcv")
    private BigDecimal tasa_bcv;

    @Column(name= "lunes_deñ_mes")
    private int lunes_del_mes;

    @ManyToOne
    @JoinColumn(name = "periodo_academico")
    private Periodo_academico periodo_academico;

    public Periodo_academico getPeriodo_academico() {
        return periodo_academico;
    }

    public void setPeriodo_academico(Periodo_academico periodo_academico) {
        this.periodo_academico = periodo_academico;
    }

    public BigDecimal getTasa_bcv() {
        return tasa_bcv;
    }

    public void setTasa_bcv(BigDecimal tasa_bcv) {
        this.tasa_bcv = tasa_bcv;
    }

    public int getLunes_del_mes() {
        return lunes_del_mes;
    }

    public void setLunes_del_mes(int lunes_del_mes) {
        this.lunes_del_mes = lunes_del_mes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    @Column
    private String estatus; // "ABIERTO", "CERRADO"
}