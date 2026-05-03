package com.hospital.utils;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service

public class EmailUtils {
   @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(String to, String subject, String text, List<String> list){
        SimpleMailMessage message =new SimpleMailMessage();

        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        if(list!= null && list.size() > 0){
            message.setCc(getCcArray(list));
            emailSender.send(message);
        }
       
    }

    private String[] getCcArray(List<String> ccList){
        String[] cc = new String[ccList.size()];

        for (int i = 0; i < cc.length; i++) {
            cc[i]=ccList.get(i);
        }
        return cc;
    }

   
}
