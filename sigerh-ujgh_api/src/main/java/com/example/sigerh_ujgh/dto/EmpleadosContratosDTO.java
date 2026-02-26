package com.example.sigerh_ujgh.dto;

import com.example.sigerh_ujgh.entity.Contrato;
import com.example.sigerh_ujgh.entity.Empleado;
import java.util.List;


public class EmpleadosContratosDTO {


        private Empleado empleado;
        private List<Contrato> contratos;
        private int cantidadContratos;
        private Contrato ultimoContrato; // Para mostrar el cargo actual en la tabla

        public Empleado getEmpleado() {
                return empleado;
        }

        public void setEmpleado(Empleado empleado) {
                this.empleado = empleado;
        }

        public List<Contrato> getContratos() {
                return contratos;
        }

        public void setContratos(List<Contrato> contratos) {
                this.contratos = contratos;
        }

        public int getCantidadContratos() {
                return cantidadContratos;
        }

        public void setCantidadContratos(int cantidadContratos) {
                this.cantidadContratos = cantidadContratos;
        }

        public Contrato getUltimoContrato() {
                return ultimoContrato;
        }

        public void setUltimoContrato(Contrato ultimoContrato) {
                this.ultimoContrato = ultimoContrato;
        }
}
