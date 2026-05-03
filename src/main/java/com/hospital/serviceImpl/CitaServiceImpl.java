package com.hospital.serviceImpl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.hospital.JWT.JwtFilter;
import com.hospital.POJO.Cita;
import com.hospital.POJO.Especialidad;
import com.hospital.POJO.Medico;
import com.hospital.POJO.User;
import com.hospital.constents.HospitalConstant;
import com.hospital.dao.CitasDao;
import com.hospital.dao.EspecialidadDao;
import com.hospital.dao.MedicoDao;
import com.hospital.dao.UserDao;
import com.hospital.service.CitaService;
import com.hospital.utils.HospitalUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CitaServiceImpl implements CitaService {

    @Autowired
    CitasDao citasDao;

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    UserDao userDao;

    @Autowired
    EmailService emailService;

    @Autowired
    MedicoDao medicoDao;

    @Autowired
    EspecialidadDao especialidadDao;

    @Override
    public ResponseEntity<List<Cita>> getAllCita(String filterValue) {
        try {
            List<Cita> citas;
            if (!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true")) {
                citas = citasDao.findAll();
            } else {
                citas = citasDao.findAll();
            }
            return new ResponseEntity<>(citas, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Error al obtener todas las citas: {}", ex.getMessage(), ex);
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> addNewCita(Map<String, String> requestMap) {
        try {
            if (validateCitaMap(requestMap)) {
                Cita cita = getCitaFromMap(requestMap, false);

                Optional<Cita> existingCitaUsuario = citasDao.findByUsuarioFechaHora(
                        cita.getUsuario().getId(),
                        cita.getFecha(),
                        cita.getHora());

                if (existingCitaUsuario.isPresent()) {
                    return HospitalUtils.getResponseEntity(
                            "Ya existe una cita para el usuario para esa fecha y hora.",
                            HttpStatus.CONFLICT);
                }

                Optional<Cita> existingCitaMedico = citasDao.findByMedicoEspecialidadFechaHora(
                        cita.getMedico().getIdMedico(),
                        cita.getEspecialidad().getId(),
                        cita.getFecha(),
                        cita.getHora());

                if (existingCitaMedico.isPresent()) {
                    return HospitalUtils.getResponseEntity(
                            "Ya existe una cita para el médico, especialidad, para esa fecha y hora.",
                            HttpStatus.CONFLICT);
                }

                // Guardar la cita
                Cita citaGuardada = citasDao.save(cita);

                // Obtener el usuario
                Optional<User> optionalUser = userDao.findById(citaGuardada.getUsuario().getId());

                if (optionalUser.isPresent()) {
                    User usuario = optionalUser.get();

                    // Obtener el médico completo con sus datos
                    String nombreMedico = "No especificado";
                    Optional<Medico> optionalMedico = medicoDao.findById(citaGuardada.getMedico().getIdMedico());
                    if (optionalMedico.isPresent()) {
                        nombreMedico = optionalMedico.get().getNombreMedico();
                    }

                    // Obtener la especialidad completa con sus datos
                    String nombreEspecialidad = "No especificada";
                    Optional<Especialidad> optionalEspecialidad = especialidadDao
                            .findById(citaGuardada.getEspecialidad().getId());
                    if (optionalEspecialidad.isPresent()) {
                        nombreEspecialidad = optionalEspecialidad.get().getNombreEspecialidad();
                    }

                    // Convertir la fecha de Date a LocalDate
                    Date fechaDate = citaGuardada.getFecha();
                    Instant instant = fechaDate.toInstant();
                    LocalDate fecha = instant.atZone(ZoneId.systemDefault()).toLocalDate();

                    // Formatear la fecha con Locale español
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd 'de' MMMM",
                            new Locale("es", "ES"));
                    String fechaFormateada = fecha.format(formatter);
                    // Capitalizar primera letra
                    fechaFormateada = fechaFormateada.substring(0, 1).toUpperCase() + fechaFormateada.substring(1);

                    // Construir el correo con toda la información
                    String emailEnvio = usuario.getEmail();
                    String cuerpo = "Confirmación de Cita - Centro Médico";
                    String asunto = "Estimado/a " + usuario.getNombre() + ",\n\n" +
                            "Su cita ha sido programada con los siguientes detalles:\n\n" +
                            "Fecha: " + fechaFormateada + "\n" +
                            "Hora: " + citaGuardada.getHora() + "\n" +
                            "Médico: " + nombreMedico + "\n" +
                            "Especialidad: " + nombreEspecialidad + "\n\n" +
                            "Por favor, llegue 15 minutos antes de su cita.\n\n" +
                            "Gracias por confiar en nosotros.\n\n" +
                            "Centro Médico";

                    emailService.sendEmail(emailEnvio, cuerpo, asunto);
                    log.info("Correo de confirmación enviado a: {}", emailEnvio);
                }

                return HospitalUtils.getResponseEntity("Cita agregada correctamente", HttpStatus.OK);
            }
            return HospitalUtils.getResponseEntity(HospitalConstant.INVALID_DATA, HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            log.error("Error al agregar nueva cita: {}", ex.getMessage(), ex);
            return HospitalUtils.getResponseEntity(HospitalConstant.SOMETHING_WENT_WRONG,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean validateCitaMap(Map<String, String> requestMap) {
        // Validar si el mapa contiene todos los campos necesarios para una cita
        return requestMap.containsKey("especialidad_id")
                && requestMap.containsKey("medico_id")
                && requestMap.containsKey("usuario_id")
                && requestMap.containsKey("fecha")
                && requestMap.containsKey("hora");
    }

    private Cita getCitaFromMap(Map<String, String> requestMap, boolean isAdd) {
        // Obtener los valores del mapa
        String especialidadIdString = requestMap.get("especialidad_id");
        String medicoIdString = requestMap.get("medico_id");
        String usuarioIdString = requestMap.get("usuario_id");
        String fechaString = requestMap.get("fecha");
        String horaString = requestMap.get("hora");

        try {
            // Parsear los valores de los ids
            int especialidadId = Integer.parseInt(especialidadIdString);
            int medicoId = Integer.parseInt(medicoIdString);
            int usuarioId = Integer.parseInt(usuarioIdString);

            // Crear objetos Especialidad, Medico y User con los ids correspondientes
            Especialidad especialidad = new Especialidad();
            especialidad.setId(especialidadId);

            Medico medico = new Medico();
            medico.setIdMedico(medicoId);

            User usuario = new User();
            usuario.setId(usuarioId);

            // Crear objeto Cita y establecer los valores
            Cita cita = new Cita();
            if (isAdd) {
                cita.setIdCita(Integer.parseInt(requestMap.get("idCita")));
            }
            cita.setMedico(medico);
            cita.setEspecialidad(especialidad);
            cita.setUsuario(usuario);

            // Parsear la fecha y la hora
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalDate fecha = LocalDate.parse(fechaString, dateFormatter);
            LocalTime hora = LocalTime.parse(horaString, timeFormatter);

            // Convertir la fecha y la hora a objetos Date
            Instant instant = LocalDateTime.of(fecha, hora).atZone(ZoneId.systemDefault()).toInstant();
            Date fechaHora = Date.from(instant);

            // Establecer la fecha y la hora en el objeto Cita
            cita.setFecha(fechaHora);
            cita.setHora(hora);

            return cita;
        } catch (NumberFormatException | DateTimeParseException e) {
            // Manejar las excepciones de parseo
            log.error("Error al parsear campos: {}", e.getMessage(), e);
            return null;
        }
    }

    // @Override
    // public ResponseEntity<String> updateCita(Map<String, String> requesMap) {
    //
    // throw new UnsupportedOperationException("Unimplemented method 'updateCita'");
    // }

    @Override
    public ResponseEntity<String> deleteCita(Integer idCita) {
        try {
            Optional<Cita> optionalCita = citasDao.findById(idCita);
            if (optionalCita.isPresent()) {
                Cita cita = optionalCita.get();
                citasDao.deleteById(idCita);

                // Obtener información del usuario asociado a la cita
                Optional<User> optionalUser = userDao.findById(cita.getUsuario().getId());
                if (optionalUser.isPresent()) {
                    User usuario = optionalUser.get();

                    // Formatear la fecha de la cita
                    Date fechaCita = cita.getFecha();
                    Instant instant = fechaCita.toInstant();
                    LocalDateTime fechaHoraCita = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd 'de' MMMM 'a las' HH:mm",
                            new Locale("es", "ES"));
                    String fechaHoraFormateada = fechaHoraCita.format(formatter);

                    // Construir el cuerpo del correo electrónico
                    String cuerpo = "Cancelación de Cita";
                    String asunto = "Estimado/a " + usuario.getNombre() + ",\n\n" +
                            "Su cita programada para el " + fechaHoraFormateada + " ha sido cancelada.\n\n" +
                            "Gracias por confiar en nosotros.";

                    // Enviar el correo electrónico
                    emailService.sendEmail(usuario.getEmail(), cuerpo, asunto);
                }

                return HospitalUtils.getResponseEntity("Cita eliminada correctamente", HttpStatus.OK);
            } else {
                return HospitalUtils.getResponseEntity("La cita no existe", HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            log.error("Error al eliminar la cita: {}", ex.getMessage(), ex);
            return HospitalUtils.getResponseEntity(HospitalConstant.SOMETHING_WENT_WRONG,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<Cita>> getCitasByUsuario(Integer id) {
        try {
            return new ResponseEntity<>(citasDao.getCitasByUsuario(id), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

// @Autowired
// CitasDao citasDao;

// @Autowired

// EspecialidadDao especialidadDao;

// @Autowired

// MedicoDao medicoDao;

// @Autowired
// MedicoService medicoService;

// @Autowired
// JwtFilter jwtFilter;

// @Override
// public ResponseEntity<List<Cita>> getAllCita(String filterValue) {
// try {
// List<Cita> citas;
// if (!Strings.isNullOrEmpty(filterValue) &&
// filterValue.equalsIgnoreCase("true")) {
// citas = citasDao.getAllCita();
// } else {
// citas = citasDao.findAll();
// }
// return new ResponseEntity<>(citas, HttpStatus.OK);
// } catch (Exception ex) {
// log.error("Error al obtener todas las citas: {}", ex.getMessage(), ex);
// return new ResponseEntity<>(new ArrayList<>(),
// HttpStatus.INTERNAL_SERVER_ERROR);
// }
// }

// @Override
// public ResponseEntity<String> addNewCita(Map<String, String> requestMap) {
// try {
// if (jwtFilter.isUser()) {
// // Validar los campos de la cita
// if (validateCitaMap(requestMap)) {
// // Crear y guardar la cita en la base de datos
// Cita cita = getCitaFromMap(requestMap, false);
// if (cita != null) {
// citasDao.save(cita);
// return HospitalUtils.getResponseEntity("Cita agregada correctamente",
// HttpStatus.OK);
// } else {
// return HospitalUtils.getResponseEntity("Error al crear la cita",
// HttpStatus.BAD_REQUEST);
// }
// }
// return HospitalUtils.getResponseEntity(HospitalConstant.INVALID_DATA,
// HttpStatus.BAD_REQUEST);
// } else {
// return HospitalUtils.getResponseEntity(HospitalConstant.UNAUTHORIZED_ACCESS,
// HttpStatus.UNAUTHORIZED);
// }
// } catch (Exception ex) {
// log.error("Error al agregar nueva cita: {}", ex.getMessage(), ex);
// return HospitalUtils.getResponseEntity(HospitalConstant.SOMETHING_WENT_WRONG,
// HttpStatus.INTERNAL_SERVER_ERROR);
// }
// }

// private boolean validateCitaMap(Map<String, String> requestMap) {
// // Validar si el mapa contiene todos los campos necesarios para una cita
// return requestMap.containsKey("especialidad_id")
// && requestMap.containsKey("medico_id")
// && requestMap.containsKey("usuario_id")
// && requestMap.containsKey("fecha")
// && requestMap.containsKey("hora");
// }

// private Cita getCitaFromMap(Map<String, String> requestMap, boolean isAdd) {
// String nombreEspecialidad = requestMap.get("especialidad_id");
// String nombreMedico = requestMap.get("medico_id");
// User usuario = new User();
// usuario.setId(Integer.parseInt(requestMap.get("usuario_id")));

// // Buscar la especialidad por su nombre
// Especialidad especialidad =
// especialidadDao.findByNombreEspecialidad(nombreEspecialidad);
// if (especialidad == null) {
// // Manejar el caso en que no se encuentre la especialidad
// log.error("Especialidad no encontrada para el nombre: {}",
// nombreEspecialidad);
// return null;
// }

// // Buscar el médico por su nombre
// Medico medico = medicoDao.findByNombreMedico(nombreMedico);
// if (medico == null) {
// // Manejar el caso en que no se encuentre el médico
// log.error("Médico no encontrado para el nombre: {}", nombreMedico);
// return null;
// }

// Cita cita = new Cita();

// if (isAdd) {
// cita.setIdCita(Integer.parseInt(requestMap.get("idCita")));
// }
// cita.setMedico(medico);
// cita.setEspecialidad(especialidad);
// String fechaString = requestMap.get("fecha");
// String horaString = requestMap.get("hora");

// DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
// DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

// try {
// // Parsear la fecha y la hora

// LocalDate fecha = LocalDate.parse(fechaString, dateFormatter);
// LocalTime hora = LocalTime.parse(horaString, timeFormatter);

// // Convertir la fecha y la hora a objetos Date
// Instant instant = LocalDateTime.of(fecha,
// hora).atZone(ZoneId.systemDefault()).toInstant();
// Date fechaHora = Date.from(instant);

// // Establecer la fecha y la hora en el objeto Cita
// cita.setFecha(fechaHora);
// cita.setHora(hora);
// } catch (DateTimeParseException e) {
// // Manejar el error de parseo de fecha u hora
// log.error("Error al parsear fecha u hora: {}", e.getMessage(), e);
// return null;
// }

// cita.setUsuario(usuario);
// return cita;
// }
