package com.hospital.dao;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hospital.POJO.Cita;

public interface CitasDao extends JpaRepository<Cita, Integer> {

    @Query("SELECT c FROM Cita c WHERE c.medico.idMedico = :medicoId AND c.especialidad.id = :especialidadId AND c.fecha = :fecha AND c.hora = :hora")
    Optional<Cita> findByMedicoEspecialidadFechaHora(
            @Param("medicoId") Integer medicoId,
            @Param("especialidadId") Integer especialidadId,
            @Param("fecha") Date fecha,
            @Param("hora") LocalTime hora);

    @Query("SELECT c FROM Cita c WHERE c.usuario.id = :usuarioId")
    List<Cita> getCitasByUsuario(@Param("usuarioId") Integer usuario);

    @Query("SELECT c FROM Cita c WHERE c.usuario.id = :usuarioId AND c.fecha = :fecha AND c.hora = :hora")
    Optional<Cita> findByUsuarioFechaHora(
            @Param("usuarioId") Integer usuarioId,
            @Param("fecha") Date fecha,
            @Param("hora") LocalTime hora);
}
