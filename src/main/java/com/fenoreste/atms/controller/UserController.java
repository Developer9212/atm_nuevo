package com.fenoreste.atms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fenoreste.atms.entity.User;
import com.fenoreste.atms.service.IUserService;

@RestController
@RequestMapping({"/users" })
public class UserController {
    
	@Autowired
	private IUserService userSevice;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/")
	public ResponseEntity<?>obtnerUsuarios(){
		List<User>users = userSevice.findAll();
		if(users != null) {
			return new ResponseEntity<>(users,HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/create_user")
	public ResponseEntity<?>crearUsuario(@RequestBody User user){
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setId(1);
		this.userSevice.save(user);
		return new ResponseEntity<>(HttpStatus.CREATED);
		
	}
	
}
