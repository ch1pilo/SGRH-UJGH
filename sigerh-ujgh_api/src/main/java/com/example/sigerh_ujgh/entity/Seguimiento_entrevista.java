package com.example.sigerh_ujgh.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
public class Seguimiento_entrevista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // Cambiado a LocalDate para que coincida con Entrevista.fecha_programada
    @Column(name = "fecha_entrevista")
    private LocalDate fecha_entrevista;

    @ManyToOne
    @JoinColumn(name = "id_entrevista")
    private Entrevista id_entrevista;

    // NUEVO: Para saber qué pasó ("POSPUESTA", "COMPLETADA", "CANCELADA")
    @Column(name = "estado", length = 50)
    private String estado;

    // Corregido el error de tipeo (obserbacion -> observacion)
    @Column(name = "observacion")
    private String observacion;

    @Column(name = "id_usuario")
    private String id_usuario;

    // --- GETTERS Y SETTERS ---

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public LocalDate getFecha_entrevista() { return fecha_entrevista; }
    public void setFecha_entrevista(LocalDate fecha_entrevista) { this.fecha_entrevista = fecha_entrevista; }

    public Entrevista getId_entrevista() { return id_entrevista; }
    public void setId_entrevista(Entrevista id_entrevista) { this.id_entrevista = id_entrevista; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    public String getId_usuario() { return id_usuario; }
    public void setId_usuario(String id_usuario) { this.id_usuario = id_usuario; }
}