package com.hospital.POJO;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@NamedQuery(name = "Cita.getAllCita", query = "select c from Cita c")
@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "citas")
public class Cita implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCita")
    private Integer idCita;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "especialidad_id", nullable = false)
    private Especialidad especialidad;

    @Column(name = "fecha", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date fecha;

    @Column(name = "hora", nullable = false, columnDefinition = "TIME")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime hora;

    public String getNombreUsuario() {
        return this.usuario != null ? this.usuario.getNombre() : null;
    }

    public String getNombreMedico() {
        return this.medico != null ? this.medico.getNombreMedico() : null;
    }

    public String getNombreEspecialidad() {
        return this.especialidad != null ? this.especialidad.getNombreEspecialidad() : null;
    }
}
