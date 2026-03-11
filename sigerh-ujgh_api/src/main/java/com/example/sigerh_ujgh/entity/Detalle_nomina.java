package com.example.sigerh_ujgh.entity;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "detalle_nomina")
public class Detalle_nomina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_nomina", nullable = false)
    @JsonIgnore
    private Nomina nomina;

    // 💡 CLASIFICADOR: "ASIGNACION_CARGA", "DESCUENTO_DEUDA", "DESCUENTO_INASISTENCIA"
    // Esto le hará la vida facilísima a tu frontend para saber si suma o resta
    @Column(name = "tipo_detalle")
    private String tipoDetalle;

    // ==========================================
    // RELACIONES DINÁMICAS (Todas deben ser nullable = true)
    // ==========================================

    // Si es un pago de horas, se llena este y los demás quedan null
    @ManyToOne
    @JoinColumn(name = "id_carga_academica", nullable = true)
    private Carga_academica cargaAcademica;

    // Si es un cobro de deuda (préstamo, adelanto), se llena este
    @ManyToOne
    @JoinColumn(name = "id_novedad_descuento", nullable = true)
    private Novedad_descuento novedadDescuento;

    // Si es un descuento por faltar, se llena este
    @ManyToOne
    @JoinColumn(name = "id_inasistencia", nullable = true)
    private Inacistencia inasistencia;

    // ==========================================
    // EL DINERO (Lo que se pagó o se descontó en este recibo)
    // ==========================================
    @Column(name = "valor")
    private BigDecimal valor;

    // En la entidad Detalle_nomina.java
    @ManyToOne
    @JoinColumn(name = "id_ajuste", nullable = true)
    private Ajuste ajuste;

    public Ajuste getAjuste() {
        return ajuste;
    }

    public void setAjuste(Ajuste ajuste) {
        this.ajuste = ajuste;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Nomina getNomina() {
        return nomina;
    }

    public void setNomina(Nomina nomina) {
        this.nomina = nomina;
    }

    public String getTipoDetalle() {
        return tipoDetalle;
    }

    public void setTipoDetalle(String tipoDetalle) {
        this.tipoDetalle = tipoDetalle;
    }

    public Carga_academica getCargaAcademica() {
        return cargaAcademica;
    }

    public void setCargaAcademica(Carga_academica cargaAcademica) {
        this.cargaAcademica = cargaAcademica;
    }

    public Novedad_descuento getNovedadDescuento() {
        return novedadDescuento;
    }

    public void setNovedadDescuento(Novedad_descuento novedadDescuento) {
        this.novedadDescuento = novedadDescuento;
    }

    public Inacistencia getInasistencia() {
        return inasistencia;
    }

    public void setInasistencia(Inacistencia inasistencia) {
        this.inasistencia = inasistencia;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}