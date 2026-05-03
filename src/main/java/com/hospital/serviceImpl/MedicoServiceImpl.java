package com.hospital.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hospital.JWT.JwtFilter;
import com.hospital.POJO.Especialidad;
import com.hospital.POJO.Medico;
import com.hospital.constents.HospitalConstant;
import com.hospital.dao.MedicoDao;
import com.hospital.service.MedicoService;
import com.hospital.utils.HospitalUtils;
import com.hospital.wrapper.MedicoWrapper;
import java.time.LocalDate;
import java.time.Period;

@Service

public class MedicoServiceImpl implements MedicoService {

    @Autowired
    MedicoDao medicoDao;

    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNewMedico(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                if (validateMedicoMap(requestMap, false)) {
                    // Verificar si el médico ya existe
                    String nombreMedico = requestMap.get("nombreMedico");
                    if (medicoDao.findByNombreMedico(nombreMedico) != null) {
                        return HospitalUtils.getResponseEntity("El médico con este nombre ya existe", HttpStatus.CONFLICT);
                    }
    
                    medicoDao.save(getMedicosFromMap(requestMap, false));
                    return HospitalUtils.getResponseEntity("Médico agregado correctamente", HttpStatus.OK);
                }
                return HospitalUtils.getResponseEntity(HospitalConstant.INVALID_DATA, HttpStatus.BAD_REQUEST);
            } else {
                return HospitalUtils.getResponseEntity(HospitalConstant.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return HospitalUtils.getResponseEntity(HospitalConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    

    private boolean validateMedicoMap(Map<String, String> requestMap, boolean validateId) {
        if (requestMap.containsKey("nombreMedico")) {
            if (requestMap.containsKey("idMedico") && validateId) {
                return true;
            } else if (!validateId) {
                return true;
            }
        }
        return false;
    }

    private Medico getMedicosFromMap(Map<String, String> requestMap, boolean isAdd) {
    Especialidad especialidad = new Especialidad();
    especialidad.setId(Integer.parseInt(requestMap.get("especialidadId")));
    
    Medico medico = new Medico();
    if (isAdd) {
        medico.setIdMedico(Integer.parseInt(requestMap.get("idMedico")));
    } else {
        medico.setStatus("true");
    }
    medico.setEspecialidadMedico(especialidad);
    medico.setNombreMedico(requestMap.get("nombreMedico"));
    
    // ✅ Verificar que telefonoMedico no sea null
    String telefonoStr = requestMap.get("telefonoMedico");
    if (telefonoStr != null && !telefonoStr.isEmpty()) {
        medico.setTelefonoMedico(Integer.parseInt(telefonoStr));
    }
    
    // ✅ USAR FECHA DE NACIMIENTO en lugar de edad
    String fechaNacimientoStr = requestMap.get("fechaNacimiento");
    if (fechaNacimientoStr != null && !fechaNacimientoStr.isEmpty()) {
        java.time.LocalDate fechaNacimiento = java.time.LocalDate.parse(fechaNacimientoStr);
        medico.setFechaNacimiento(fechaNacimiento);
        
        // Calcular edad automáticamente
        int edad = java.time.Period.between(fechaNacimiento, java.time.LocalDate.now()).getYears();
        medico.setEdadMedico(edad);
    }
    
    medico.setImagenMedico(requestMap.get("imagenMedico"));
    return medico;
}

    @Override
    public ResponseEntity<List<MedicoWrapper>> getAllMedico() {
        try {
            return new ResponseEntity<>(medicoDao.getAllMedico(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateMedico(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                if (validateMedicoMap(requestMap, true)) {
                    Optional<Medico> optional = medicoDao.findById(Integer.parseInt(requestMap.get("idMedico")));
    
                    if (optional.isPresent()) {
                        Medico existingMedico = optional.get();
                        String newNombreMedico = requestMap.get("nombreMedico");
                      
    
                        // Verificar si el nombre del médico ya existe en otro registro
                        Medico medicoByName = medicoDao.findByNombreMedico(newNombreMedico);
                        if (medicoByName != null && !medicoByName.getIdMedico().equals(existingMedico.getIdMedico())) {
                            return HospitalUtils.getResponseEntity("El nombre del médico ya existe", HttpStatus.BAD_REQUEST);
                        }
    
                        // Verificar si la imagen del médico ya existe en otro registro
                       
    
                        Medico medico = getMedicosFromMap(requestMap, true);
                        medico.setStatus(existingMedico.getStatus());
                        medicoDao.save(medico);
                        return HospitalUtils.getResponseEntity("Médico actualizado correctamente", HttpStatus.OK);
                    } else {
                        return HospitalUtils.getResponseEntity("ID del médico no existe", HttpStatus.BAD_REQUEST);
                    }
                } else {
                    return HospitalUtils.getResponseEntity(HospitalConstant.INVALID_DATA, HttpStatus.BAD_REQUEST);
                }
            } else {
                return HospitalUtils.getResponseEntity(HospitalConstant.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return HospitalUtils.getResponseEntity(HospitalConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    

    @Override
    public ResponseEntity<String> deleteMedico(Integer id) {
        try {

            if (jwtFilter.isAdmin()) {

                Optional optional = medicoDao.findById(id);
                if (!optional.isEmpty()) {
                    medicoDao.deleteById(id);
                    return HospitalUtils.getResponseEntity("Medico eliminado correctamente", HttpStatus.OK);
                }

                return HospitalUtils.getResponseEntity("El medico no existe", HttpStatus.OK);
            } else {
                return HospitalUtils.getResponseEntity(HospitalConstant.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return HospitalUtils.getResponseEntity(HospitalConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional optional = medicoDao.findById(Integer.parseInt(requestMap.get("idMedico")));

                if (!optional.isEmpty()) {
                    medicoDao.updateMedicoStatus(requestMap.get("status"),
                            Integer.parseInt(requestMap.get("idMedico")));
                    return HospitalUtils.getResponseEntity("Estado del medico actualizado", HttpStatus.OK);
                }
                return HospitalUtils.getResponseEntity("El id del medico no existe", HttpStatus.OK);
            } else {
                return HospitalUtils.getResponseEntity(HospitalConstant.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return HospitalUtils.getResponseEntity(HospitalConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<MedicoWrapper>> getByEspecialidad(Integer id) {
        try {
            return new ResponseEntity<>(medicoDao.getMedicosByEspecialidad(id), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<MedicoWrapper> getMedicoById(Integer id) {
        try {
            return new ResponseEntity<>(medicoDao.getMedicosById(id), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new MedicoWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    public Medico findByNombreMedico(String nombreMedico) {
        return medicoDao.findByNombreMedico(nombreMedico);
    }

}