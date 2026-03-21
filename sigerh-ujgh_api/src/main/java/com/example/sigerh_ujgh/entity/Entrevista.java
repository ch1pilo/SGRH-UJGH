package com.example.sigerh_ujgh.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class Entrevista {

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public LocalDateTime getFecha_programada() {
        return fecha_programada;
    }

    public void setFecha_programada(LocalDateTime fecha_programada) {
        this.fecha_programada = fecha_programada;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn (name = "id_persona", nullable = false)
    private Persona persona;

    @Column
    @JsonProperty("fechaProgramada") // Para que React lo envíe como fechaProgramada
    private LocalDateTime fecha_programada;

    @Column(columnDefinition = "TEXT")
    private String observacion;

    public Boolean getEstatus() {
        return estatus;
    }

    public void setEstatus(Boolean estatus) {
        this.estatus = estatus;
    }

    @Column()
    private Boolean estatus = true;

}
