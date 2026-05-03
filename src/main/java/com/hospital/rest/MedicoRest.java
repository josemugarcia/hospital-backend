package com.hospital.rest;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hospital.wrapper.MedicoWrapper;

@RequestMapping(path = "/medico")
public interface MedicoRest {

    @PostMapping(path = "/add")

    ResponseEntity<String> addNewMedico(@RequestBody Map<String, String> requestMap);

    @GetMapping(path = "/get")

    ResponseEntity<List<MedicoWrapper>> getAllMedico();

    @PostMapping(path = "/update")

    ResponseEntity<String> updateMedico(@RequestBody Map<String, String> requestMap);

    @DeleteMapping(path = "/delete/{id}")
    ResponseEntity<String> deleteMedico(@PathVariable Integer idMedico);

    @PostMapping(path = "/updateStatus")

    ResponseEntity<String> updateStatus(@RequestBody Map<String, String> requestMap);

    @GetMapping(path = "/getByEspecialidad/{id}")

    ResponseEntity<List<MedicoWrapper>> getByEspecialidad(@PathVariable Integer id);

    @GetMapping(path = "/getById/{id}")

    ResponseEntity<MedicoWrapper> getMedicoById(@PathVariable Integer id);

}
