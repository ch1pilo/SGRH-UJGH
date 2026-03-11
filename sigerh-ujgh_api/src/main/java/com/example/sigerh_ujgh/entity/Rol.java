package com.example.sigerh_ujgh.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nombre; // Ej: "ADMIN", "RECURSOS_HUMANOS"

    // Relación: Un Rol tiene muchas Vistas. Crea la tabla 'rol_vista' automáticamente.
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "rol_vista",
            joinColumns = @JoinColumn(name = "rol_id"),
            inverseJoinColumns = @JoinColumn(name = "vista_id")
    )
    private Set<Vista> vistasPermitidas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Vista> getVistasPermitidas() {
        return vistasPermitidas;
    }

    public void setVistasPermitidas(Set<Vista> vistasPermitidas) {
        this.vistasPermitidas = vistasPermitidas;
    }
}
