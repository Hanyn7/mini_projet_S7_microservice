package com.hanin.users.restControllers;

import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.hanin.parfums.security.SecParams;
import com.hanin.users.entities.Role;
import com.hanin.users.entities.User;
import com.hanin.users.entities.VerificationRequest;
import com.hanin.users.service.EmailService;
import com.hanin.users.service.RoleService;
import com.hanin.users.service.UserService;


@RestController
@CrossOrigin(origins = "*")
public class UserRestController {
	
	 @Autowired
	    UserService userService;
	 @Autowired
	    RoleService roleService;
	 @Autowired
	    EmailService emailService;
	 
	    @RequestMapping(path = "all", method = RequestMethod.GET)
	    public List<User> getAllUsers() {
	        return userService.findAllUsers();
	    }

	    @RequestMapping(path = "add", method = RequestMethod.POST)
	    public User saveUser(@RequestBody User user) {
	        return userService.saveUser(user);
	    }

	    @RequestMapping(path = "addRole/{id}", method = RequestMethod.POST)
	    public User addRoleToUser(@PathVariable long id, @RequestBody Role r) {
	        return userService.addRoleToUser(id, r);
	    }

	    @RequestMapping(path = "findUserById/{id}", method = RequestMethod.GET)
	    public User findUserById(@PathVariable Long id) {
	        return userService.findUserById(id);
	    }

	    @RequestMapping(path = "allRoles", method = RequestMethod.GET)
	    public List<Role> getAllRoles() {
	        return userService.findAllRoles();
	    }

	    @RequestMapping(path = "findRoleById/{id}", method = RequestMethod.GET)
	    public Role findRoleById(@PathVariable Long id) {
	        return userService.findRoleById(id);
	    }

	    @RequestMapping(path = "deleteUserById/{id}", method = RequestMethod.DELETE)
	    public void deleteUserById(@PathVariable long id) {
	        userService.deleteUser(id);
	    }

	    @RequestMapping(path = "removeRoleFromUer/{id}", method = RequestMethod.POST)
	    public User removeRole(@PathVariable long id, @RequestBody Role r) {
	        return userService.removeRoleFromUser(id, r);

	    }
	    @PostMapping("/register")
	    public ResponseEntity<Void> registerUser(@RequestBody User user) {
	        String verificationCode = generateVerificationCode();
	      
	        User newUser = new User();
	        newUser.setUsername(user.getUsername());
	        newUser.setPassword(user.getPassword());
	        newUser.setEnabled(true);
	        newUser.setEmail(user.getEmail());
	        newUser.setVerificationCode(verificationCode);
	        
	        Role userRole = roleService.findRoleByName("USER");
	        if (userRole != null) {
	            newUser.setRoles(Collections.singletonList(userRole));
	        }
	        
	        userService.saveUser(newUser);

	        String emailSubject = "Verification Code";
	        String emailBody = "Your verification code is: " + verificationCode;
	        sendVerificationCodeByEmail(newUser.getEmail(), emailSubject, emailBody);

	        return ResponseEntity.ok().build();
	    }

	    @PostMapping("/verify")
	    public ResponseEntity<Void> verifyCode(@RequestBody VerificationRequest request) {
	        User user = userService.findUserByUsername(request.getUsername());

	        if (user == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	        }

	        if (user.getVerificationCode().equals(request.getVerificationCode())) {
	            String jwt = JWT.create()
	                    .withSubject(user.getUsername())
	                    .withExpiresAt(new Date(System.currentTimeMillis() + SecParams.EXP_TIME))
	                    .sign(Algorithm.HMAC256(SecParams.SECRET));

	            HttpHeaders headers = new HttpHeaders();
	            headers.add("Authorization", jwt);

	            return ResponseEntity.ok().headers(headers).build();
	        }

	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }

	    public void sendVerificationCodeByEmail(String to, String subject, String body) {
	        emailService.sendEmail(to, subject, body);
	    }
	    public String generateVerificationCode() {
	        Random random = new Random();
	        int code = 100000 + random.nextInt(900000);
	        return String.valueOf(code);
	    }


}