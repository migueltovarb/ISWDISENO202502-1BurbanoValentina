package com.crud.vehiculo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crud.vehiculo.model.Vehiculo;
import com.crud.vehiculo.service.VehiculoService;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {

    private final VehiculoService vehiculoService;

    public VehiculoController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    @GetMapping
    public List<Vehiculo> listarVehiculos() {
        return vehiculoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehiculo> obtenerVehiculo(@PathVariable String id) {
        Vehiculo vehiculo = vehiculoService.obtenerPorId(id);
        return vehiculo != null ? ResponseEntity.ok(vehiculo) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Vehiculo> crearVehiculo(@RequestBody Vehiculo vehiculo) {
        Vehiculo creado = vehiculoService.crear(vehiculo);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehiculo> actualizarVehiculo(@PathVariable String id, @RequestBody Vehiculo vehiculo) {
        if (vehiculoService.obtenerPorId(id) == null) {
            return ResponseEntity.notFound().build();
        }
        Vehiculo actualizado = vehiculoService.actualizar(id, vehiculo);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVehiculo(@PathVariable String id) {
        if (vehiculoService.obtenerPorId(id) == null) {
            return ResponseEntity.notFound().build();
        }
        vehiculoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
