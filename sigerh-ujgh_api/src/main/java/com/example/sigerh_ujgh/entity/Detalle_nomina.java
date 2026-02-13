package com.example.sigerh_ujgh.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "detalle_nomina")
public class Detalle_nomina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Vinculación con la cabecera (Lote)
    @ManyToOne
    @JoinColumn(name = "id_nomina", nullable = false)
    private Nomina nomina;


    // --- INGRESOS ---
    @Column
    private Integer horasTrabajadas; // Cantidad de horas calculadas

    @Column
    private BigDecimal sueldoBase; // Dinero por horas normales

    @Column
    private BigDecimal totalBonos; // Bono nocturno + Fines de semana

    // --- EGRESOS (Deducciones) ---
    @Column
    private BigDecimal deduccionesLey; // SSO + FAOV + PIE (Calculado automático)

    @Column
    private BigDecimal deduccionesNovedades; // Préstamos + Faltas (Viene de NovedadDescuento)

    // --- TOTAL ---
    @Column(nullable = false)
    private BigDecimal sueldoNeto; // Lo que le llega al banco

    @Column(columnDefinition = "TEXT")
    private String logCalculo; // Guardaremos un resumen: "4h Nocturnas + 2h Diurnas..."
}