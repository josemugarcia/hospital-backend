package com.hospital.POJO;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Data;

// @NamedQuery(name = "Especialidad.getAllEspecialidad", query = "select e from Especialidad e ")
@NamedQuery(name = "Especialidad.getAllEspecialidad", query = "select e from Especialidad e where e.id in (select m.especialidadMedico from Medico m where m.status= 'true') ")

@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "especialidad")
public class Especialidad implements Serializable{
    private static final long serialVersionUID= 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id")
    private Integer id;

    @Column(name = "nombreEspecialidad")
    private String nombreEspecialidad;

    @Column(name = "imagenEspecialidad")
    private String imagenEspecialidad;

    
    
}
