package com.example.sigerh_ujgh.dto;

import com.example.sigerh_ujgh.entity.Contrato;
import com.example.sigerh_ujgh.entity.Empleado;
import lombok.Data;
import java.util.List;

@Data
public class EmpleadosContratosDTO {

        private Empleado empleado;
        private List<Contrato> contratos;
        private int cantidadContratos;
        private Contrato ultimoContrato; // Para mostrar el cargo actual en la tabla
    
}
