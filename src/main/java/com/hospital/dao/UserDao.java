package com.hospital.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.hospital.POJO.User;
import com.hospital.wrapper.UserWrapper;

public interface UserDao extends JpaRepository<User, Integer> {

    User findByEmailId(String email);

    User findByEmail(String email);
    
    User findByResetToken(String resetToken);
    

    @Query("select new com.hospital.wrapper.UserWrapper(u.id, u.nombre, u.email, u.telefono, u.estado, u.password, u.nombreCompleto, u.edad) from User u where u.rol='user'")
    List<UserWrapper> getAllUser();
    
    @Query("select new com.hospital.wrapper.UserWrapper(u.id, u.nombre, u.email, u.telefono, u.estado, u.password, u.nombreCompleto, u.edad) from User u where u.rol='admin'")
    List<UserWrapper> getAdmin();
    
    @Modifying
    @Transactional
    @Query("update User u set u.estado=:estado where u.id=:id")
    Integer updateStatus(@Param("estado") String estado, @Param("id") Integer id);
}