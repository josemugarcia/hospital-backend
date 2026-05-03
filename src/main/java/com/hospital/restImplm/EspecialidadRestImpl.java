package com.hospital.restImplm;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.POJO.Especialidad;
import com.hospital.constents.HospitalConstant;
import com.hospital.rest.EspecialidadRest;
import com.hospital.service.EspecialidadService;
import com.hospital.utils.HospitalUtils;

@RestController
public class EspecialidadRestImpl implements EspecialidadRest {

    @Autowired
    EspecialidadService especialidadService;

    @Override
    public ResponseEntity<String> addNewEspecialidad(Map<String, String> requestMap) {
        try {
            return especialidadService.addNewEspecialidad(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return HospitalUtils.getResponseEntity(HospitalConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Especialidad>> getAllEspecialidad(String filterValue) {
        try {
            return especialidadService.getAllEspecialidad(filterValue);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> updateEspecialidad(Map<String, String> requestMap) {
        try {
            return especialidadService.updateEspecialidad(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return HospitalUtils.getResponseEntity(HospitalConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
