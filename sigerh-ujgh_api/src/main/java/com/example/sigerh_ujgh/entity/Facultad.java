package com.example.sigerh_ujgh.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data // Esto genera los getters y setters automáticamente
@Entity // Esto le dice a Spring que es una tabla
@Table(name = "facultad")
public class Facultad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public Boolean getEstatus() {
        return estatus;
    }

    public void setEstatus(Boolean estatus) {
        this.estatus = estatus;
    }

    @Column
    private Boolean estatus = true;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(unique = true, length = 20)
    private String codigo;

}