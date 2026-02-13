package com.example.sigerh_ujgh.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "tipo_descuento")
public class Tipo_descuento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre; // Ej: "Caja de Ahorro", "Prestamo Personal"

    @Column
    private String descripcion;

    @Column
    private Boolean activo = true;
}