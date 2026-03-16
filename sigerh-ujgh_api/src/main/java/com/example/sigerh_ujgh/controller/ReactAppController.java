package com.example.sigerh_ujgh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;

@Controller
public class ReactAppController {

    @GetMapping(value = {
            "/",
            "/login",
            "/usuarios",
            "/Dashboard",
            "/ajustes",
            "/lotes",
            "/lotes/**",            // El /** permite sub-rutas como /lotes/editar/1
            "/carga-academica",
            "/actividadesAdmin",
            "/entrevistas",
            "/detalle_nomina",
            "/nomina",
            "/descuentos",
            "/inasistencias",
            "/tipos-descuento",
            "/turnos",
            "/tipo_contratos",
            "/tabulador",
            "/reclutamiento",
            "/configuracion",
            "/empleados",
            "/empleados/**",        // Permite sub-rutas de empleados
            "/facultades",
            "/periodos",
            "/contratos"
    })
    public String forwardToReact() {
        // Redirige todo al frontend de React
        return "forward:/index.html";
    }
}
