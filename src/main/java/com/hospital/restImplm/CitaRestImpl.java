package com.hospital.restImplm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.POJO.Cita;
import com.hospital.constents.HospitalConstant;
import com.hospital.rest.CitaRest;
import com.hospital.service.CitaService;
import com.hospital.utils.HospitalUtils;

@RestController

public class CitaRestImpl implements CitaRest {

   @Autowired
   CitaService citaService;
   @Override
   public ResponseEntity<List<Cita>> getAllCita(String filterValue) {
      try {
         return citaService.getAllCita(filterValue);
      } catch (Exception ex) {
         ex.printStackTrace();
      }
      return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
   }

   @Override
   public ResponseEntity<String> addNewCita(Map<String, String> requestMap) {
       try {
            return citaService.addNewCita(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return HospitalUtils.getResponseEntity(HospitalConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
   }

   // @Override
   // public ResponseEntity<String> updateCita(Map<String, String> requestMap) {
   //    try {
   //       return citaService.updateCita(requestMap);
   //  } catch (Exception ex) {
   //   ex.printStackTrace();
   //  }
   //  return HospitalUtils.getResponseEntity(HospitalConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
   // }

   @Override
    public ResponseEntity<String> deleteCita(Integer idCita) {

        try {
            return citaService.deleteCita(idCita);
       } catch (Exception ex) {
        ex.printStackTrace();
       }
       return HospitalUtils.getResponseEntity(HospitalConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

   @Override
   public ResponseEntity<List<Cita>> getCitasByUsuario(Integer id) {
      try {
         return citaService.getCitasByUsuario(id);
       } catch (Exception ex) {
         ex.printStackTrace();
       }
       return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
   }


}