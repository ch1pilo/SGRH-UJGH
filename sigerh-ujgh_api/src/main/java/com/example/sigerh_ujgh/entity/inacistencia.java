package com.example.sigerh_ujgh.entity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;


public class inacistencia {

    @Entity
    @Table(name = "inasistencia")
    public class Inasistencia {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private LocalDate fecha;

        @Column(nullable = false)
        private Integer horas;

        // USO BIGDECIMAL: Para coincidir con tu Service y evitar errores de redondeo
        @Column(name = "monto_total", nullable = false, precision = 10, scale = 2)
        private BigDecimal montoTotal;

        // USO BIGDECIMAL: Para guardar factores exactos (ej: 1.5, 2.0)
        @Column(name = "factor_aplicado", nullable = false, precision = 5, scale = 2)
        private BigDecimal factorAplicado;

        @Column(length = 255)
        private String observacion;

        // --- RELACIONES ---

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "id_empleado", nullable = false)
        private Empleado empleado;


        // --- CONSTRUCTORES ---

        public Inasistencia() {
        }

        // --- GETTERS Y SETTERS ---

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public LocalDate getFecha() {
            return fecha;
        }

        public void setFecha(LocalDate fecha) {
            this.fecha = fecha;
        }

        public Integer getHoras() {
            return horas;
        }

        public void setHoras(Integer horas) {
            this.horas = horas;
        }

        public BigDecimal getMontoTotal() {
            return montoTotal;
        }

        public void setMontoTotal(BigDecimal montoTotal) {
            this.montoTotal = montoTotal;
        }

        public BigDecimal getFactorAplicado() {
            return factorAplicado;
        }

        public void setFactorAplicado(BigDecimal factorAplicado) {
            this.factorAplicado = factorAplicado;
        }

        public String getObservacion() {
            return observacion;
        }

        public void setObservacion(String observacion) {
            this.observacion = observacion;
        }

        public Empleado getEmpleado() {
            return empleado;
        }

        public void setEmpleado(Empleado empleado) {
            this.empleado = empleado;
        }



    }
}
