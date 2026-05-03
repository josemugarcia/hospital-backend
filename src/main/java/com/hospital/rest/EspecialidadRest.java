package com.hospital.rest;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hospital.POJO.Especialidad;

@RequestMapping(path = "/especialidad")
public interface EspecialidadRest {

    @PostMapping(path = "/add")
    ResponseEntity<String> addNewEspecialidad(@RequestBody(required = true) Map<String, String> requestMap);

    @GetMapping(path= "/get")
    ResponseEntity<List<Especialidad>> getAllEspecialidad(@RequestParam(required = false) String filterValue);

    @PostMapping(path = "/update")
    ResponseEntity<String> updateEspecialidad(@RequestBody(required = true) Map<String,String> requestMap); 
}