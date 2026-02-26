package com.example.sigerh_ujgh.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data

public class Tabulador_salarial {

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public float getSueldo_base() {
        return sueldo_base;
    }

    public void setSueldo_base(float sueldo_base) {
        this.sueldo_base = sueldo_base;
    }

    public float getValor_hora() {
        return valor_hora;
    }

    public void setValor_hora(float valor_hora) {
        this.valor_hora = valor_hora;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private Boolean activo = true;

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @Column
    private String nivel;
    @Column
    private float sueldo_base;
    @Column
    private float valor_hora;

}
