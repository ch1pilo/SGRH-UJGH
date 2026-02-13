package com.example.sigerh_ujgh.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

import static java.lang.Boolean.*;

@Entity
@Data

public class Turno {

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalTime getHora_inicio() {
        return hora_inicio;
    }

    public void setHora_inicio(LocalTime hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public LocalTime getHora_fin() {
        return hora_fin;
    }

    public void setHora_fin(LocalTime hora_fin) {
        this.hora_fin = hora_fin;
    }

    public boolean isNocturno() {
        return nocturno;
    }

    public void setNocturno(boolean nocturno) {
        this.nocturno = nocturno;
    }

    public boolean isFin_de_semana() {
        return fin_de_semana;
    }

    public void setFin_de_semana(boolean fin_de_semana) {
        this.fin_de_semana = fin_de_semana;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String nombre;
    @Column
    private LocalTime hora_inicio;
    @Column
    private LocalTime hora_fin;
    @Column
    private boolean nocturno = false;
    @Column
    private boolean fin_de_semana = false;
}
