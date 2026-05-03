package com.hospital.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.hospital.POJO.Cita;

public interface CitaService {

    ResponseEntity<List<Cita>> getAllCita(String filterValue);

    ResponseEntity<String> addNewCita(Map<String, String> requestMap);

    // ResponseEntity<String> updateCita(Map<String, String> requesMap);

    ResponseEntity<String> deleteCita(Integer idCita);

    ResponseEntity<List<Cita>> getCitasByUsuario(Integer id);

}