package com.hospital.wrapper;

import java.time.LocalDate;
import lombok.Data;

@Data
public class MedicoWrapper {
    Integer idMedico;
    String nombreMedico;
    Integer telefonoMedico;
    Integer edadMedico;
    String imagenMedico;
    String status;
    Integer especialidadId;
    String especialidadNombre;
    String especialidadImagen;
    LocalDate fechaNacimiento;  // ✅ NUEVO CAMPO

    public MedicoWrapper() {

    }

    // Constructor original (sin fecha de nacimiento)
    public MedicoWrapper(Integer idMedico, String nombreMedico, Integer telefonoMedico, Integer edadMedico,
            String imagenMedico, String status, Integer especialidadId, String especialidadNombre,
            String especialidadImagen) {
        this.idMedico = idMedico;
        this.nombreMedico = nombreMedico;
        this.telefonoMedico = telefonoMedico;
        this.edadMedico = edadMedico;
        this.imagenMedico = imagenMedico;
        this.status = status;
        this.especialidadId = especialidadId;
        this.especialidadNombre = especialidadNombre;
        this.especialidadImagen = especialidadImagen;
    }

    // ✅ NUEVO CONSTRUCTOR (con fecha de nacimiento)
    public MedicoWrapper(Integer idMedico, String nombreMedico, Integer telefonoMedico, Integer edadMedico,
            String imagenMedico, String status, Integer especialidadId, String especialidadNombre,
            String especialidadImagen, LocalDate fechaNacimiento) {
        this(idMedico, nombreMedico, telefonoMedico, edadMedico, imagenMedico, 
             status, especialidadId, especialidadNombre, especialidadImagen);
        this.fechaNacimiento = fechaNacimiento;
    }

    // Constructor simple
    public MedicoWrapper(Integer idMedico, String nombreMedico){
        this.idMedico = idMedico;
        this.nombreMedico = nombreMedico;
    }
}