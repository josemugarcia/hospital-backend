package com.hospital.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hospital.POJO.Especialidad;

public interface EspecialidadDao extends JpaRepository<Especialidad,Integer> {
    List<Especialidad> getAllEspecialidad();
    Especialidad findByNombreEspecialidad(String nombreEspecialidad);

}
