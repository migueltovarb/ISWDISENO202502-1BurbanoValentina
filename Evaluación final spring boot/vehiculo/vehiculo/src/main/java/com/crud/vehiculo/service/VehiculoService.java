package com.crud.vehiculo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.crud.vehiculo.model.Vehiculo;
import com.crud.vehiculo.repository.VehiculoRepository;

@Service
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;

    public VehiculoService(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    public List<Vehiculo> listarTodos() {
        return vehiculoRepository.findAll();
    }

    public Vehiculo obtenerPorId(String id) {
        return vehiculoRepository.findById(id).orElse(null);
    }

    public Vehiculo crear(Vehiculo vehiculo) {
        return vehiculoRepository.save(vehiculo);
    }

    public Vehiculo actualizar(String id, Vehiculo vehiculo) {
        vehiculo.setId(id);
        return vehiculoRepository.save(vehiculo);
    }

    public void eliminar(String id) {
        vehiculoRepository.deleteById(id);
    }
}
