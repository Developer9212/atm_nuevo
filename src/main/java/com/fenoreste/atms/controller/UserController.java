package com.fenoreste.atms.controller;

import java.util.Date;
import java.util.Random;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fenoreste.atms.entity.User;
import com.fenoreste.atms.service.IUserService;

@RestController
@RequestMapping({"/users" })
@AllArgsConstructor
public class UserController {
    

	private final IUserService userSevice;
	private  final BCryptPasswordEncoder bCryptPasswordEncoder;

	
	@PostMapping("/create_user")
	public ResponseEntity<User>crearUsuario(@RequestBody User user){
		String psw = user.getPassword();
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setCreate_at(new Date());

		Random random = new Random();
		int value = random.nextInt(50 + 1) +1;
		user.setId(value);
		User newUser = userSevice.save(user);

		if(newUser.getUsername().toUpperCase().contains("EXISTE")){
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}else{
			newUser.setPassword(psw);
			return new ResponseEntity<>(newUser,HttpStatus.CREATED);
		}

		
	}
	
}
