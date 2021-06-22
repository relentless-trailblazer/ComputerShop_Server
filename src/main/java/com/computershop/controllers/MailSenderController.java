package com.computershop.controllers;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.crypto.codec.Utf8;
//import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.computershop.exceptions.InvalidException;
import com.computershop.helpers.Validate;
import com.computershop.models.Email;

@RestController
@RequestMapping("/api/mail")
public class MailSenderController {
	
	@Autowired
	private JavaMailSender mailSender;

	@PostMapping("/simple")
	@PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
	public ResponseEntity<?> sendSimpleEmail(@RequestBody Email email) {
		// validate mail
		if(Validate.isEmail(email.getEmail())==false)
			throw new InvalidException("Invalid email format");
		//send mail
		SimpleMailMessage mail = new SimpleMailMessage();
		// add data to mail
		mail.setTo(email.getEmail()); // can send to many user, : setTo(mail_user1, mail_user2, ...)
		mail.setSubject("A system email from TTA computer shop");
		mail.setText(email.getMessage());
		// send and return result
        mailSender.send(mail);
        return ResponseEntity.ok().body("Email Sent!");
	}
	

    @PostMapping("/attachments")
    @PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
    public ResponseEntity<?> sendAttachmentEmail(@RequestBody Email email, @RequestBody MultipartFile[] files) throws MessagingException {
    	// validate mail
    	if(Validate.isEmail(email.getEmail())==false)
			throw new InvalidException("Invalid email format");
    	
    	//Send mail
    	MimeMessage mail = mailSender.createMimeMessage();
        boolean multipart = true;
        MimeMessageHelper helper = new MimeMessageHelper(mail, multipart, "utf-8");
        helper.setTo(email.getEmail());
        helper.setSubject("A system email with attachments from TTA computer shop");   
        helper.setText(email.getMessage());
        // add attachments to mail 
        for(int i = 0; i < files.length; i++) {
        	helper.addAttachment("file "+ i,files[i]);
        }
        mailSender.send(mail);
        return ResponseEntity.ok().body( "Email Sent!");
    }
    
    
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody String email) throws MessagingException {
    	// validate mail
    	if(Validate.isEmail(email)==false)
			throw new InvalidException("Invalid email format");
		//send mail
		SimpleMailMessage mail = new SimpleMailMessage();
		
		mail.setTo(email); 
		mail.setSubject("A system email from TTA computer shop");
		mail.setText("Your new password: ");
		
        mailSender.send(mail);
        return ResponseEntity.ok().body("Email Sent!");
    	
    }
}
