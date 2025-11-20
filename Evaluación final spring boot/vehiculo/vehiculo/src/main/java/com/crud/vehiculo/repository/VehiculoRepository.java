package com.crud.vehiculo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.crud.vehiculo.model.Vehiculo;

@Repository
public interface VehiculoRepository extends MongoRepository<Vehiculo, String> {
}
