package com.example.sigerh_ujgh.controller;
import com.example.sigerh_ujgh.entity.Tipo_descuento;
import com.example.sigerh_ujgh.service.TipoDescuentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tipo_descuento")
@CrossOrigin("*") // Permite conexión desde React
public class tipodescuentoController {

        @Autowired
        private TipoDescuentoService service;

        // Obtener todos
        @GetMapping
        public List<Tipo_descuento> listar() {
            return service.listar();
        }

        // Guardar o Actualizar
        @PostMapping
        public Tipo_descuento guardar(@RequestBody Tipo_descuento obj) {
            return service.guardar(obj);
        }

        // Eliminar
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> eliminar(@PathVariable Long id) {
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        }

}
