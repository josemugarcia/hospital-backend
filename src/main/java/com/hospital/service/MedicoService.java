package com.hospital.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.hospital.wrapper.MedicoWrapper;

public interface MedicoService {

    ResponseEntity<String> addNewMedico(Map<String, String> requestMap);

    ResponseEntity<List<MedicoWrapper>> getAllMedico();

    ResponseEntity<String> updateMedico(Map<String, String> requesMap);

    ResponseEntity<String> deleteMedico(Integer id);

    ResponseEntity<String> updateStatus(Map<String, String> requestMap);

    ResponseEntity<List<MedicoWrapper>> getByEspecialidad(Integer id);

    ResponseEntity<MedicoWrapper> getMedicoById(Integer id);

   
}


