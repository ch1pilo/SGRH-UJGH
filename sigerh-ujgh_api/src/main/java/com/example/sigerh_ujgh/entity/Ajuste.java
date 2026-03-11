package com.example.sigerh_ujgh.entity;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Ajuste {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        // Relación con el empleado (Asumiendo que tienes una entidad Empleado)
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "empleado_id", nullable = false)
        private Empleado empleado;

        @Column(nullable = false)
        private String tipo; // "POSITIVO" o "NEGATIVO"

        @Column(nullable = false)
        private Double monto;

        @Column(nullable = false)
        private String motivo; // Ej: "Faltó bono quincena pasada"

        // Esto es CLAVE: Nos dice si ya se pagó/descontó en una nómina o si sigue pendiente
        @Column(nullable = false)
        private Boolean procesado = false;

        @Column(name = "fecha_registro")
        private LocalDate fechaRegistro;

        @PrePersist
        protected void onCreate() {
            this.fechaRegistro = LocalDate.now();
        }

        // --- GETTERS Y SETTERS ---
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public Empleado getEmpleado() { return empleado; }
        public void setEmpleado(Empleado empleado) { this.empleado = empleado; }

        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }

        public Double getMonto() { return monto; }
        public void setMonto(Double monto) { this.monto = monto; }

        public String getMotivo() { return motivo; }
        public void setMotivo(String motivo) { this.motivo = motivo; }

        public Boolean getProcesado() { return procesado; }
        public void setProcesado(Boolean procesado) { this.procesado = procesado; }

        public LocalDate getFechaRegistro() { return fechaRegistro; }
        public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    }

