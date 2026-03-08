package com.example.sigerh_ujgh.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Carga_administrativa {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "id_carga_academica")
    private Carga_academica id_carga_academica;

    @ManyToOne
    @JoinColumn(name = "id_actividad_administrativa")
    private Tipo_actividades_administrativas id_actividad_administrativa;

    @Column (name = "hora_actividad")
    private Integer hora_actividad;

    public Integer getHora_actividad() {
        return hora_actividad;
    }

    public void setHora_actividad(Integer hora_actividad) {
        this.hora_actividad = hora_actividad;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Carga_academica getId_carga_academica() {
        return id_carga_academica;
    }

    public void setId_carga_academica(Carga_academica id_carga_academica) {
        this.id_carga_academica = id_carga_academica;
    }

    public Tipo_actividades_administrativas getId_actividad_administrativa() {
        return id_actividad_administrativa;
    }

    public void setId_actividad_administrativa(Tipo_actividades_administrativas id_actividad_administrativa) {
        this.id_actividad_administrativa = id_actividad_administrativa;
    }
}
