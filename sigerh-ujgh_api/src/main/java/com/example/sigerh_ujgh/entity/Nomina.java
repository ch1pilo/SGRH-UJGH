package com.example.sigerh_ujgh.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "nomina")
public class Nomina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con el Lote (Padre)
    @ManyToOne
    @JoinColumn(name = "id_lote_nomina", nullable = false)
    private LoteNomina loteNomina;

    // Relación con el Empleado
    @ManyToOne
    @JoinColumn(name = "id_empleado", nullable = false)
    private Empleado empleado;

    @Column
    private BigDecimal hora_fin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LoteNomina getLoteNomina() {
        return loteNomina;
    }

    public void setLoteNomina(LoteNomina loteNomina) {
        this.loteNomina = loteNomina;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public BigDecimal getHora_fin() {
        return hora_fin;
    }

    public void setHora_fin(BigDecimal hora_fin) {
        this.hora_fin = hora_fin;
    }

    public BigDecimal getHora_nocturna() {
        return hora_nocturna;
    }

    public void setHora_nocturna(BigDecimal hora_nocturna) {
        this.hora_nocturna = hora_nocturna;
    }

    public BigDecimal getHora_diurna() {
        return hora_diurna;
    }

    public void setHora_diurna(BigDecimal hora_diurna) {
        this.hora_diurna = hora_diurna;
    }



    public BigDecimal getSueldo_base() {
        return sueldo_base;
    }

    public void setSueldo_base(BigDecimal sueldo_base) {
        this.sueldo_base = sueldo_base;
    }

    public BigDecimal getMonto_cestaticket() {
        return monto_cestaticket;
    }

    public void setMonto_cestaticket(BigDecimal monto_cestaticket) {
        this.monto_cestaticket = monto_cestaticket;
    }

    public BigDecimal getTotal_ingreso() {
        return total_ingreso;
    }

    public void setTotal_ingreso(BigDecimal total_ingreso) {
        this.total_ingreso = total_ingreso;
    }


    public BigDecimal getMonto_sso() {
        return monto_sso;
    }

    public void setMonto_sso(BigDecimal monto_sso) {
        this.monto_sso = monto_sso;
    }

    public BigDecimal getMonto_spe() {
        return monto_spe;
    }

    public void setMonto_spe(BigDecimal monto_spe) {
        this.monto_spe = monto_spe;
    }

    public BigDecimal getMonto_faov() {
        return monto_faov;
    }

    public void setMonto_faov(BigDecimal monto_faov) {
        this.monto_faov = monto_faov;
    }

    public BigDecimal getMonto_descuento_hora() {
        return monto_descuento_hora;
    }

    public void setMonto_descuento_hora(BigDecimal monto_descuento_hora) {
        this.monto_descuento_hora = monto_descuento_hora;
    }

    public BigDecimal getOtros_descuento() {
        return otros_descuento;
    }

    public void setOtros_descuento(BigDecimal otros_descuento) {
        this.otros_descuento = otros_descuento;
    }

    public BigDecimal getTotal_deducciones() {
        return total_deducciones;
    }

    public void setTotal_deducciones(BigDecimal total_deducciones) {
        this.total_deducciones = total_deducciones;
    }

    public BigDecimal getNeto_a_pagar() {
        return neto_a_pagar;
    }

    public void setNeto_a_pagar(BigDecimal neto_a_pagar) {
        this.neto_a_pagar = neto_a_pagar;
    }

    public BigDecimal getTotal_bs() {
        return total_bs;
    }

    public void setTotal_bs(BigDecimal total_bs) {
        this.total_bs = total_bs;
    }

    @Column
    private BigDecimal hora_nocturna;

    @Column
    private BigDecimal hora_diurna;

    @Column
    private BigDecimal sueldo_base;

    @Column
    private BigDecimal monto_cestaticket;

    @Column
    private BigDecimal total_ingreso;

    @Column
    private BigDecimal monto_sso;

    @Column
    private BigDecimal monto_spe;

    @Column
    private BigDecimal monto_faov;

    @Column
    private BigDecimal monto_descuento_hora;

    @Column
    private BigDecimal otros_descuento;

    @Column
    private BigDecimal total_deducciones;

    @Column
    private BigDecimal neto_a_pagar;

    @Column
    private BigDecimal total_bs;

}