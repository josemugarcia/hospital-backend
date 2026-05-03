package com.hospital.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.hospital.POJO.Especialidad;

public interface EspecialidadService {

    ResponseEntity<String> addNewEspecialidad(Map<String, String> requestMap);

    ResponseEntity<List<Especialidad>> getAllEspecialidad(String filterValue);

    ResponseEntity<String> updateEspecialidad(Map<String, String> requestMap);

}
