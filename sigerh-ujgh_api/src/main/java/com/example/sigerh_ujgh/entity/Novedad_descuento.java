package com.example.sigerh_ujgh.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "novedad_descuento")
public class Novedad_descuento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_tipo_desceunto", nullable = false)
    private Tipo_descuento tipoDescuento;

    @ManyToOne
    @JoinColumn(name = "id_empleado", nullable = false)
    private Empleado empleado;

    @Column(precision = 10, scale = 2)
    private BigDecimal monto_total_deuda;

    @Column(precision = 10, scale = 2)
    private BigDecimal cuota_por_nomina;

    @Column(precision = 10, scale = 2)
    private BigDecimal saldo_pendiente;


    @Column
    private LocalDate fecha_carga;

    @Column
    private String observacion;

    @Column
    private Boolean procesado;


}
