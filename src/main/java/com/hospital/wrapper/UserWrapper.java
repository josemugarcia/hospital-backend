package com.hospital.wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserWrapper {

    private Integer id;
    private String nombre;          // Nombre de usuario (apodo)
    private String email;
    private String telefono;
    private String estado;
    private String password;
    private String nombreCompleto;  // ✅ NUEVO: Nombre real completo
    private Integer edad;           // ✅ NUEVO: Edad calculada

    // Constructor original (sin nuevos campos)
    public UserWrapper(Integer id, String nombre, String email, String telefono, String estado, String password) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.estado = estado;
        this.password = password;
    }

    // ✅ NUEVO CONSTRUCTOR con todos los campos
    public UserWrapper(Integer id, String nombre, String email, String telefono, String estado, 
                       String password, String nombreCompleto, Integer edad) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.estado = estado;
        this.password = password;
        this.nombreCompleto = nombreCompleto;
        this.edad = edad;
    }
}