package com.fenoreste.atms.service;

import java.util.List;

import com.fenoreste.atms.entity.User;


public interface IUserService {
   
	public List<User>findAll();
	
	public User save(User user);
	
	public User findById(Integer id);
}
