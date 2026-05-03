package com.hospital.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.hospital.wrapper.UserWrapper;

public interface UserService {
    ResponseEntity<String> signUp(Map<String, String> requestMap);

    ResponseEntity<String> login(Map<String, String> requestMap);

    ResponseEntity<List<UserWrapper>> getAllUser();

    ResponseEntity<List<UserWrapper>> getAdmin();

    ResponseEntity<String> updateUser(Map<String, String> requestMap);

    ResponseEntity<String> checkToken();

    ResponseEntity<String> changePassword(Map<String, String> requestMap);

    ResponseEntity<String> deleteUser(Integer id);

    ResponseEntity<String> updateEstado(Map<String, String> requestMap);

    ResponseEntity<String> forgotPassword(Map<String, String> requestMap);

    ResponseEntity<String> resetPassword(Map<String, String> requestMap);
}
