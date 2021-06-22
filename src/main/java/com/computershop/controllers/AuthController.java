package com.computershop.controllers;

import java.util.Optional;

//import javax.mail.MessagingException;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.computershop.dao.User;
import com.computershop.dto.ForgotPasswordDTO;
import com.computershop.dto.SignUpDTO;
import com.computershop.exceptions.DuplicateException;
import com.computershop.exceptions.InvalidException;
import com.computershop.exceptions.NotFoundException;
import com.computershop.helpers.ConvertObject;
import com.computershop.helpers.GenerateRandomPassword;
import com.computershop.helpers.Validate;
//import com.computershop.helpers.Validate;
import com.computershop.models.AuthenticationRequest;
import com.computershop.models.AuthenticationResponse;
import com.computershop.repositories.UserRepository;
import com.computershop.services.MyUserDetailsService;
import com.computershop.utils.JwtUtil;

@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private MyUserDetailsService myUserDetailsService;
	
	@Autowired
	public PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JavaMailSender mailSender;
	
	
	
	@PostMapping("/login") 
	public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
		try {			
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		} catch (Exception e) {
			throw new LoginException("Incorrect username or password!");
		}
		final UserDetails userDetails = myUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String jwt = jwtUtil.generateToken(userDetails);
		User user = userRepository.findByUsername(authenticationRequest.getUsername());
		return ResponseEntity.ok().body(new AuthenticationResponse(jwt, user.getId(), user.getUsername(), user.getRole()));
	}
	
	@PostMapping("/validate")
	public ResponseEntity<?> validateToken(HttpServletRequest request,
			@RequestBody AuthenticationResponse authenticationResponse) {
		try {
			String jwt = authenticationResponse.getJwt();
			String username = jwtUtil.extractUsername(jwt);
			UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
			if (jwtUtil.validateToken(jwt, userDetails)) {
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
			User user = userRepository.findByUsername(username);
			return ResponseEntity.ok(new AuthenticationResponse(jwtUtil.generateToken(userDetails), user.getId(),
					user.getUsername(), user.getRole()));
		} catch (Exception e) {
			throw new InvalidException(e.getMessage());
		}
	}
	
	
	@PostMapping("/signup")
	public ResponseEntity<?> signUp(@RequestBody SignUpDTO signUpDTO){
		if(Validate.checkSignUp(signUpDTO)) {
			throw new InvalidException("Invalid infomation!"); 
		}
		User oldUser = userRepository.findByUsername(signUpDTO.getUsername());
		if(oldUser != null) {
			throw new DuplicateException("Username has been taken!");
		}
		
		User user = ConvertObject.fromSignUpDTOToUserDAO(signUpDTO);
		if(user == null) {
			throw new InvalidException("Invalid user");
		}
		user.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));
		User newUser = userRepository.save(user);

		final UserDetails userDetails = myUserDetailsService.loadUserByUsername(newUser.getUsername());
		final String jwt = jwtUtil.generateToken(userDetails);
		return ResponseEntity.ok(new AuthenticationResponse(jwt, newUser.getId(), newUser.getUsername(), newUser.getRole()));
	}
	
	@PostMapping("/forgot-password")
	public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordDTO forgotPasswordDTO) throws Exception {
		// get user
		Optional<User> optionalUser = userRepository.findById(forgotPasswordDTO.getUserId());
        if (!optionalUser.isPresent()) {
            throw new NotFoundException("User not found");
        }
		User user = optionalUser.get();
        
		// generatePassword
		GenerateRandomPassword generatePassword = new GenerateRandomPassword();
		String newPassword = generatePassword.getNewPassword();
		
		//Change password
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
        
        // send new password to user email
		String email = user.getEmail();
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(email); 
		mail.setSubject("A system email from TTA computer shop");
		mail.setText("Your new password: " + newPassword);
	    mailSender.send(mail);
	    
	    return ResponseEntity.ok().body("User's password has changed and sent to your email!");	
	}



}
