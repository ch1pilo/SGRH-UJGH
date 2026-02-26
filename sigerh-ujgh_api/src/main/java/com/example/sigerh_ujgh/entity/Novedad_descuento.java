package com.example.sigerh_ujgh.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tipo_descuento getTipoDescuento() {
        return tipoDescuento;
    }

    public void setTipoDescuento(Tipo_descuento tipoDescuento) {
        this.tipoDescuento = tipoDescuento;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public BigDecimal getMonto_total_deuda() {
        return monto_total_deuda;
    }

    public void setMonto_total_deuda(BigDecimal monto_total_deuda) {
        this.monto_total_deuda = monto_total_deuda;
    }

    public BigDecimal getCuota_por_nomina() {
        return cuota_por_nomina;
    }

    public void setCuota_por_nomina(BigDecimal cuota_por_nomina) {
        this.cuota_por_nomina = cuota_por_nomina;
    }

    public BigDecimal getSaldo_pendiente() {
        return saldo_pendiente;
    }

    public void setSaldo_pendiente(BigDecimal saldo_pendiente) {
        this.saldo_pendiente = saldo_pendiente;
    }

    public LocalDate getFecha_carga() {
        return fecha_carga;
    }

    public void setFecha_carga(LocalDate fecha_carga) {
        this.fecha_carga = fecha_carga;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Boolean getProcesado() {
        return procesado;
    }

    public void setProcesado(Boolean procesado) {
        this.procesado = procesado;
    }
}
