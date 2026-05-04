package com.hospital.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.hospital.JWT.CustomerUserDetailsService;
import com.hospital.JWT.JwtFilter;
import com.hospital.JWT.JwtUtil;
import com.hospital.POJO.User;
import com.hospital.constents.HospitalConstant;
import com.hospital.service.UserService;
import com.hospital.utils.HospitalUtils;
import com.hospital.wrapper.UserWrapper;
import com.hospital.dao.UserDao;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomerUserDetailsService customerUserDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    EmailService emailService;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("inside signup {}", requestMap);
        try {
            if (validateSignUpMap(requestMap)) {
                User user = userDao.findByEmailId(requestMap.get("email"));
                String nombreUsuario = requestMap.get("nombre");
                if (Objects.isNull(user)) {
                    userDao.save(getUserFromMap(requestMap));

                    String emailEnvio = requestMap.get("email");
                    String asunto = "Gracias por registrarte en nuestro sistema. Tu cuenta ha sido creada exitosamente.";
                    String cuerpo = "Bienvenido, " + nombreUsuario + "!";
                    emailService.sendEmail(emailEnvio, cuerpo, asunto);

                    return HospitalUtils.getResponseEntity("Successfully Registered. ", HttpStatus.OK);
                } else {
                    return HospitalUtils.getResponseEntity("El email ya existe", HttpStatus.BAD_REQUEST);
                }
            } else {
                return HospitalUtils.getResponseEntity(HospitalConstant.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return HospitalUtils.getResponseEntity(HospitalConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateSignUpMap(Map<String, String> requestMap) {
        if (requestMap.containsKey("nombre") && requestMap.containsKey("nombreCompleto") &&
                requestMap.containsKey("telefono") && requestMap.containsKey("email") &&
                requestMap.containsKey("password") && requestMap.containsKey("fechaNacimiento")) {
            return true;
        }
        return false;
    }

    private User getUserFromMap(Map<String, String> requestMap) {
        User user = new User();
        user.setNombre(requestMap.get("nombre")); // Nombre de usuario
        user.setNombreCompleto(requestMap.get("nombreCompleto")); // ✅ NUEVO
        user.setTelefono(requestMap.get("telefono"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(new BCryptPasswordEncoder().encode(requestMap.get("password")));
        user.setEstado("true");
        user.setRol("user");

        // ✅ Fecha de nacimiento
        String fechaNacimientoStr = requestMap.get("fechaNacimiento");
        if (fechaNacimientoStr != null && !fechaNacimientoStr.isEmpty()) {
            java.time.LocalDate fechaNacimiento = java.time.LocalDate.parse(fechaNacimientoStr);
            user.setFechaNacimiento(fechaNacimiento);
            // La edad se calculará automáticamente en @PrePersist
        }

        return user;
    }

    private User getUser2FromMap(Map<String, String> requestMap, User existingUser, boolean isAdd) {
          log.info("📝 getUser2FromMap - nombreCompleto recibido: {}", requestMap.get("nombreCompleto"));
    log.info("📝 getUser2FromMap - fechaNacimiento recibido: {}", requestMap.get("fechaNacimiento"));
        User user = new User();
        if (isAdd) {
            user.setId(Integer.parseInt(requestMap.get("id")));
        }

        // ✅ Mantener los valores existentes POR DEFECTO
        user.setNombre(requestMap.getOrDefault("nombre", existingUser.getNombre()));
        user.setNombreCompleto(requestMap.getOrDefault("nombreCompleto", existingUser.getNombreCompleto()));
        user.setTelefono(requestMap.getOrDefault("telefono", existingUser.getTelefono()));
        user.setEmail(requestMap.getOrDefault("email", existingUser.getEmail()));

        // ✅ Mantener fecha de nacimiento si no viene en la petición
        String fechaNacimientoStr = requestMap.get("fechaNacimiento");
        if (fechaNacimientoStr != null && !fechaNacimientoStr.isEmpty()) {
            java.time.LocalDate fechaNacimiento = java.time.LocalDate.parse(fechaNacimientoStr);
            user.setFechaNacimiento(fechaNacimiento);
        } else {
            user.setFechaNacimiento(existingUser.getFechaNacimiento());
        }

        // Mantener los valores existentes de estado, password y rol
        user.setEstado(existingUser.getEstado());
        user.setPassword(existingUser.getPassword()); // Mantener contraseña actual
        user.setRol(existingUser.getRol());

        // Cargar las citas asociadas al usuario
        existingUser.getCitas().size();
        user.setCitas(existingUser.getCitas());

        return user;
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("========== INICIO LOGIN ==========");
        log.info("Email recibido: {}", requestMap.get("email"));
        log.info("Password recibida: {}", requestMap.get("password"));

        try {
            // 1. Verificar si el usuario existe en BD
            User userFromDb = userDao.findByEmailId(requestMap.get("email"));
            if (userFromDb == null) {
                log.error("❌ Usuario NO encontrado: {}", requestMap.get("email"));
                return new ResponseEntity<String>("{\"message\":\"Usuario no encontrado\"}", HttpStatus.BAD_REQUEST);
            }

            log.info("✅ Usuario encontrado: {}", userFromDb.getNombre());
            log.info("Password hash en BD: {}", userFromDb.getPassword());
            log.info("Estado: {}", userFromDb.getEstado());
            log.info("Rol: {}", userFromDb.getRol());

            // 2. Verificar manualmente con BCrypt
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            boolean passwordMatches = encoder.matches(requestMap.get("password"), userFromDb.getPassword());
            log.info("🔐 ¿Coinciden las contraseñas? {}", passwordMatches);

            if (!passwordMatches) {
                log.error("❌ Contraseña incorrecta");
                return new ResponseEntity<String>("{\"message\":\"Contraseña incorrecta\"}", HttpStatus.BAD_REQUEST);
            }

            // 3. Verificar estado del usuario
            if (!userFromDb.getEstado().equalsIgnoreCase("true")) {
                log.error("❌ Usuario inactivo");
                return new ResponseEntity<String>("{\"message\":\"Wait for admin approval.\"}",
                        HttpStatus.UNAUTHORIZED);
            }

            // 4. Generar token manualmente
            String token = jwtUtil.generateToken(userFromDb.getEmail(), userFromDb.getRol());
            String nombreUsuario = userFromDb.getNombre();
            String rolUsuario = userFromDb.getRol();
            int idUsuario = userFromDb.getId();

            JSONObject responseJson = new JSONObject();
            responseJson.put("token", token);
            responseJson.put("nombreUsuario", nombreUsuario);
            responseJson.put("rolUsuario", rolUsuario);
            responseJson.put("idUsuario", idUsuario);

            log.info("✅ Login exitoso para: {}", requestMap.get("email"));
            log.info("========== FIN LOGIN EXITOSO ==========");
            return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);

        } catch (Exception ex) {
            log.error("❌ Error en login: {}", ex.getMessage());
            ex.printStackTrace();
        }

        log.error("========== FIN LOGIN FALLIDO ==========");
        return new ResponseEntity<String>("{\"message\":\"Bad Credentials.\"}", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try {
            if (jwtFilter.isAdmin()) {
                return new ResponseEntity<>(userDao.getAllUser(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateUser(Map<String, String> requestMap) {
        try {
             log.info("📝 Datos recibidos en updateUser: {}", requestMap);
            if (jwtFilter.isAdmin()) {
                if (validateUserMap(requestMap, true)) {
                    Optional<User> optional = userDao.findById(Integer.parseInt(requestMap.get("id")));

                    if (optional.isPresent()) {
                        User existingUser = optional.get();

                        // Actualizar el usuario solo si se proporciona un correo electrónico válido y
                        // único
                        String newEmail = requestMap.get("email");
                        User emailByName = userDao.findByEmail(newEmail);
                        if (emailByName == null || emailByName.getId().equals(existingUser.getId())) {
                            // Obtener el usuario actualizado desde el mapa y el usuario existente
                            User user = getUser2FromMap(requestMap, existingUser, true);
                            // Actualizar la contraseña si se proporciona en la solicitud
                            String newPassword = requestMap.get("password");
                            if (newPassword != null && !newPassword.isEmpty()) {
                                user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
                            }
                            // Guardar el usuario actualizado en la base de datos
                            userDao.save(user);
                            return HospitalUtils.getResponseEntity("Usuario actualizado correctamente", HttpStatus.OK);
                        } else {
                            return HospitalUtils.getResponseEntity("El email del usuario ya existe",
                                    HttpStatus.BAD_REQUEST);
                        }
                    } else {
                        return HospitalUtils.getResponseEntity("ID del usuario no existe", HttpStatus.BAD_REQUEST);
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

    private boolean validateUserMap(Map<String, String> requestMap, boolean validateId) {
        if (requestMap.containsKey("nombre")) {
            if (requestMap.containsKey("id") && validateId) {
                return true;
            } else if (!validateId) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ResponseEntity<String> checkToken() {
        return HospitalUtils.getResponseEntity("true", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            User userObj = userDao.findByEmail(jwtFilter.getCurrentUser());
            if (!userObj.equals(null)) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                if (encoder.matches(requestMap.get("oldPassword"), userObj.getPassword())) {
                    userObj.setPassword(encoder.encode(requestMap.get("newPassword")));
                    userDao.save(userObj);
                    return HospitalUtils.getResponseEntity("Password Updated Succesfully", HttpStatus.OK);
                }
                return HospitalUtils.getResponseEntity("Incorrect Old Password", HttpStatus.BAD_REQUEST);
            }
            return HospitalUtils.getResponseEntity(HospitalConstant.SOMETHING_WENT_WRONG,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return HospitalUtils.getResponseEntity(HospitalConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateEstado(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional<User> optional = userDao.findById(Integer.parseInt(requestMap.get("id")));

                if (optional.isPresent()) {
                    userDao.updateStatus(requestMap.get("estado"), Integer.parseInt(requestMap.get("id")));
                    return HospitalUtils.getResponseEntity("Estado del usuario actualizado", HttpStatus.OK);
                } else {
                    return HospitalUtils.getResponseEntity("El id del usuario no existe", HttpStatus.OK);
                }
            } else {
                return HospitalUtils.getResponseEntity(HospitalConstant.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return HospitalUtils.getResponseEntity(HospitalConstant.SOMETHING_WENT_WRONG,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> deleteUser(Integer id) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional<User> optional = userDao.findById(id);
                if (optional.isPresent()) {
                    User user = optional.get();
                    String email = user.getEmail();

                    userDao.deleteById(id);

                    String cuerpo = "Lamentamos informarle que su cuenta ha sido eliminada de nuestro centro.";
                    String asunto = "Cuenta Eliminada";
                    emailService.sendEmail(email, cuerpo, asunto);

                    return HospitalUtils.getResponseEntity("Usuario eliminado correctamente", HttpStatus.OK);
                }
                return HospitalUtils.getResponseEntity("El usuario no existe", HttpStatus.OK);
            } else {
                return HospitalUtils.getResponseEntity(HospitalConstant.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return HospitalUtils.getResponseEntity(HospitalConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAdmin() {
        try {
            if (jwtFilter.isAdmin()) {
                return new ResponseEntity<>(userDao.getAdmin(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
            String email = requestMap.get("email");

            if (email == null || email.isEmpty()) {
                return HospitalUtils.getResponseEntity("El email es requerido", HttpStatus.BAD_REQUEST);
            }

            User user = userDao.findByEmailId(email);

            if (user == null) {
                // Por seguridad, no revelar si el email existe o no
                return HospitalUtils.getResponseEntity("Si el email existe, recibirás un correo con instrucciones",
                        HttpStatus.OK);
            }

            // Generar token único
            String resetToken = UUID.randomUUID().toString();

            // Guardar token y expiración (24 horas)
            user.setResetToken(resetToken);
            user.setResetTokenExpiry(LocalDateTime.now().plusHours(24));
            userDao.save(user);

            // Construir enlace de recuperación
            String frontendUrl = "http://localhost:4200";
            String resetLink = frontendUrl + "/reset-password/" + resetToken;

            // Enviar correo
            String asunto = "Recuperar Contraseña - Centro Médico";
            String cuerpo = "Estimado/a "
                    + (user.getNombreCompleto() != null ? user.getNombreCompleto() : user.getNombre()) + ",\n\n"
                    + "Hemos recibido una solicitud para restablecer tu contraseña.\n\n"
                    + "Para crear una nueva contraseña, haz clic en el siguiente enlace:\n"
                    + resetLink + "\n\n"
                    + "Este enlace expirará en 24 horas.\n\n"
                    + "Si no solicitaste este cambio, ignora este mensaje.\n\n"
                    + "Saludos,\n"
                    + "Centro Médico";

            emailService.sendEmail(email, asunto, cuerpo);

            log.info("Correo de recuperación enviado a: {}", email);

            return HospitalUtils.getResponseEntity("Si el email existe, recibirás un correo con instrucciones",
                    HttpStatus.OK);

        } catch (Exception ex) {
            log.error("Error en forgotPassword: {}", ex.getMessage(), ex);
        }
        return HospitalUtils.getResponseEntity(HospitalConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> resetPassword(Map<String, String> requestMap) {
        try {
            String token = requestMap.get("token");
            String newPassword = requestMap.get("newPassword");

            if (token == null || token.isEmpty() || newPassword == null || newPassword.isEmpty()) {
                return HospitalUtils.getResponseEntity("Datos inválidos", HttpStatus.BAD_REQUEST);
            }

            // Buscar usuario por token
            User user = userDao.findByResetToken(token);

            if (user == null) {
                return HospitalUtils.getResponseEntity("Token inválido", HttpStatus.BAD_REQUEST);
            }

            // Verificar si el token ha expirado
            if (user.getResetTokenExpiry() == null || user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
                return HospitalUtils.getResponseEntity("El enlace ha expirado. Solicita un nuevo restablecimiento.",
                        HttpStatus.BAD_REQUEST);
            }

            // Actualizar la contraseña
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(newPassword));

            // Limpiar el token
            user.setResetToken(null);
            user.setResetTokenExpiry(null);

            userDao.save(user);

            log.info("Contraseña actualizada para usuario: {}", user.getEmail());

            // ✅ Enviar correo de confirmación de cambio de contraseña
            try {
                String asunto = "Tu contraseña ha sido cambiada - Centro Médico";
                String cuerpo = String.format(
                        "Estimado/a %s,\n\n" +
                                "Te confirmamos que tu contraseña ha sido cambiada exitosamente.\n\n" +
                                "Si no realizaste este cambio, por favor contacta con nosotros inmediatamente.\n\n" +
                                "Si tú realizaste el cambio, puedes ignorar este mensaje.\n\n" +
                                "Saludos,\n" +
                                "Centro Médico",
                        user.getNombreCompleto() != null ? user.getNombreCompleto() : user.getNombre());

                emailService.sendEmail(user.getEmail(), asunto, cuerpo);
                log.info("Correo de confirmación de cambio de contraseña enviado a: {}", user.getEmail());
            } catch (Exception emailEx) {
                log.error("Error al enviar correo de confirmación: {}", emailEx.getMessage());
                // No fallar el proceso si el correo no se envía
            }

            return HospitalUtils.getResponseEntity("Contraseña actualizada correctamente", HttpStatus.OK);

        } catch (Exception ex) {
            log.error("Error en resetPassword: {}", ex.getMessage(), ex);
        }
        return HospitalUtils.getResponseEntity(HospitalConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}