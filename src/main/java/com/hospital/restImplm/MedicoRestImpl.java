package com.hospital.restImplm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.constents.HospitalConstant;
import com.hospital.rest.MedicoRest;
import com.hospital.service.MedicoService;
import com.hospital.utils.HospitalUtils;
import com.hospital.wrapper.MedicoWrapper;

@RestController

public class MedicoRestImpl implements MedicoRest{

    @Autowired
    MedicoService medicoService;

    @Override
    public ResponseEntity<String> addNewMedico(Map<String, String> requestMap) {
        try {
            return medicoService.addNewMedico(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return HospitalUtils.getResponseEntity(HospitalConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<MedicoWrapper>> getAllMedico() {

        try {
            return medicoService.getAllMedico();
        } catch (Exception ex) {
           ex.printStackTrace();
        }
     return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateMedico(Map<String, String> requestMap) {
       try {
            return medicoService.updateMedico(requestMap);
       } catch (Exception ex) {
        ex.printStackTrace();
       }
       return HospitalUtils.getResponseEntity(HospitalConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteMedico(Integer id) {

        try {
            return medicoService.deleteMedico(id);
       } catch (Exception ex) {
        ex.printStackTrace();
       }
       return HospitalUtils.getResponseEntity(HospitalConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {

        try {
            return medicoService.updateStatus(requestMap);
        } catch (Exception ex) {
           ex.printStackTrace();
        }
       return HospitalUtils.getResponseEntity(HospitalConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<List<MedicoWrapper>> getByEspecialidad(Integer id) {
      try {
        return medicoService.getByEspecialidad(id);
      } catch (Exception ex) {
        ex.printStackTrace();
      }
      return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<MedicoWrapper> getMedicoById(Integer id) {
        try {
            return medicoService.getMedicoById(id);
        } catch (Exception ex) {
           ex.printStackTrace();
        }
        return new ResponseEntity<>(new MedicoWrapper(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

   
    
}
