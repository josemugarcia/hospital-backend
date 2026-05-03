package com.hospital.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class HospitalUtils {
    private HospitalUtils(){

    }

    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus){
        return new ResponseEntity<String>("{\"message\":\""+responseMessage+"\"}", httpStatus);
    }
}
