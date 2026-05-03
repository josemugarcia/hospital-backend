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
import org.springframework.web.bind.annotation.RequestParam;

import com.hospital.POJO.Cita;

@RequestMapping(path = "/cita")
public interface CitaRest {

    @GetMapping(path = "/get")

    ResponseEntity<List<Cita>> getAllCita(@RequestParam(required = false) String filterValue);

    @PostMapping(path = "/add")

    ResponseEntity<String> addNewCita(@RequestBody Map<String, String> requestMap);

    // @PostMapping(path = "/update")

    // ResponseEntity<String> updateCita(@RequestBody Map<String, String> requestMap);

    @DeleteMapping(path = "/delete/{idCita}")
    ResponseEntity<String> deleteCita(@PathVariable Integer idCita);

    @GetMapping(path = "/getCitasByUsuario/{id}")
    ResponseEntity<List<Cita>> getCitasByUsuario(@PathVariable Integer id);
    
    

}
