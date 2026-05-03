package com.hospital.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.hospital.POJO.Medico;
import com.hospital.wrapper.MedicoWrapper;

public interface MedicoDao extends JpaRepository<Medico, Integer> {
    List<MedicoWrapper> getAllMedico();

    @Modifying
    @Transactional
    Integer updateMedicoStatus(@Param("status") String status, @Param("idMedico") Integer idMedico);

    List<MedicoWrapper> getMedicosByEspecialidad(@Param("idMedico") Integer idMedico);

    MedicoWrapper getMedicosById(@Param("idMedico") Integer idMedico);

    Medico findByNombreMedico(String nombreMedico);
}
