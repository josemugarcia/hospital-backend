package com.hospital.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.hospital.JWT.JwtFilter;
import com.hospital.POJO.Especialidad;
import com.hospital.constents.HospitalConstant;
import com.hospital.dao.EspecialidadDao;
import com.hospital.service.EspecialidadService;
import com.hospital.utils.HospitalUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service

public class EspecialidadServiceImpl implements EspecialidadService {

    @Autowired

    EspecialidadDao especialidadDao;
    

    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNewEspecialidad(Map<String, String> requestMap) {
       try {
        if(jwtFilter.isAdmin()){
            if(validateEspecialidadMap(requestMap,false)){
                especialidadDao.save(getEspecialidadFromMap(requestMap, false));
            
                return HospitalUtils.getResponseEntity("Especialidad Agregada con éxito", HttpStatus.OK);
            }
        }else{
            return HospitalUtils.getResponseEntity(HospitalConstant.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        }
       } catch (Exception ex) {
      ex.printStackTrace();
       }
       return HospitalUtils.getResponseEntity(HospitalConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateEspecialidadMap(Map<String, String> requestMap, boolean validateId) {
        if(requestMap.containsKey("nombreEspecialidad")){
            if (requestMap.containsKey("id") && validateId) {
                return true;
            }else if (!validateId){
                return true;
            }
        }
        return true;
    
    }

    private Especialidad getEspecialidadFromMap(Map<String,String> requestMap, Boolean isAdd){
        Especialidad especialidad = new Especialidad();

        if(isAdd){
            especialidad.setId(Integer.parseInt(requestMap.get("id")));
        }
        especialidad.setNombreEspecialidad(requestMap.get("nombreEspecialidad"));
        especialidad.setImagenEspecialidad(requestMap.get("imagenEspecialidad"));

        return especialidad;

        
    }

    @Override
    public ResponseEntity<List<Especialidad>> getAllEspecialidad(String filterValue) {
       try {
        if(!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true")){
           log.info("Inside if"); 
            return new ResponseEntity<List<Especialidad>>(especialidadDao.getAllEspecialidad(),HttpStatus.OK);
        }

        return new ResponseEntity<>(especialidadDao.findAll(),HttpStatus.OK);
       } catch (Exception ex) {
      ex.printStackTrace();
       }
       return new ResponseEntity<List<Especialidad>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }



    @Override
    public ResponseEntity<String> updateEspecialidad(Map<String, String> requestMap) {
        try {
            if(jwtFilter.isAdmin()){
                if(validateEspecialidadMap(requestMap, true)){
                    Optional optional = especialidadDao.findById(Integer.parseInt(requestMap.get("id")));
                
                    if(!optional.isEmpty()){

                        especialidadDao.save(getEspecialidadFromMap(requestMap, true));

                        return HospitalUtils.getResponseEntity("Especialidad actualizada con éxito", HttpStatus.OK);
                    }else{
                        return HospitalUtils.getResponseEntity("Especialidad id no existe",HttpStatus.OK);
                    }
                }

                return HospitalUtils.getResponseEntity(HospitalConstant.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }else{
                return HospitalUtils.getResponseEntity(HospitalConstant.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
         ex.printStackTrace();
        }
        return HospitalUtils.getResponseEntity(HospitalConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

  
    public Especialidad findByNombreEspecialidad(String nombreEspecialidad) {
        return especialidadDao.findByNombreEspecialidad(nombreEspecialidad);
    }
    
}
