package com.hospital.POJO;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;


@NamedQuery(name = "Medico.getAllMedico", query = "select new com.hospital.wrapper.MedicoWrapper(m.idMedico,m.nombreMedico,m.telefonoMedico,m.edadMedico,m.imagenMedico,m.status,m.especialidadMedico.id,m.especialidadMedico.nombreEspecialidad,m.especialidadMedico.imagenEspecialidad, m.fechaNacimiento) from Medico m")

@NamedQuery(name = "Medico.updateMedicoStatus", query = "update Medico m set m.status=:status where m.idMedico=:idMedico")

@NamedQuery(name = "Medico.getMedicosByEspecialidad", query = "select new com.hospital.wrapper.MedicoWrapper(m.idMedico,m.nombreMedico,m.telefonoMedico,m.edadMedico,m.imagenMedico,m.status,m.especialidadMedico.id,m.especialidadMedico.nombreEspecialidad,m.especialidadMedico.imagenEspecialidad) from Medico m where m.especialidadMedico.id=:idMedico and m.status='true' ")

@NamedQuery(name = "Medico.getMedicosById", query = "select new com.hospital.wrapper.MedicoWrapper(m.idMedico,m.nombreMedico,m.telefonoMedico,m.edadMedico,m.imagenMedico,m.status,m.especialidadMedico.id,m.especialidadMedico.nombreEspecialidad,m.especialidadMedico.imagenEspecialidad) from Medico m where m.idMedico=:idMedico")

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "medicos")
public class Medico implements Serializable {

    public static final Long serialVersionUid = 123456L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMedico")
    private Integer idMedico;

    @Column(name = "nombreMedico")
    private String nombreMedico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "especialidad_fk", nullable = false)
    private Especialidad especialidadMedico;

    @Column(name = "telefonoMedico")
    private Integer telefonoMedico;

    @Column(name = "edadMedico")
    private Integer edadMedico;

    // ✅ NUEVO CAMPO
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "imagenMedico")
    private String imagenMedico;

    @Column(name = "status")
    private String status;
    
    @JsonIgnore
    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL)
    private List<Cita> citas;

    // ✅ MÉTODO PARA CALCULAR EDAD AUTOMÁTICAMENTE ANTES DE GUARDAR
    @PrePersist
    @PreUpdate
    public void calcularEdad() {
        if (fechaNacimiento != null) {
            this.edadMedico = Period.between(fechaNacimiento, LocalDate.now()).getYears();
        }
    }
}