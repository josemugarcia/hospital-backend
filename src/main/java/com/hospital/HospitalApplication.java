package com.hospital;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import com.hospital.serviceImpl.EmailService;

@SpringBootApplication
public class HospitalApplication {
  

	// @Autowired
	// private EmailService emailService;
	public static void main(String[] args) {
		SpringApplication.run(HospitalApplication.class, args);	
	}

	//    @EventListener(ApplicationReadyEvent.class) 
    //  public void sendEmailAfterStartup() {
    //      emailService.sendEmail("joselito.3417@gmail.com", "Esto es el asunto", "Esto es el cuerpo");
    //  }

}
