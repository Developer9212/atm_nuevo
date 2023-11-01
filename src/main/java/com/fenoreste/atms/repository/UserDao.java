package com.fenoreste.atms.repository;

import org.springframework.data.repository.CrudRepository;

import com.fenoreste.atms.entity.User;


public interface UserDao extends CrudRepository<User, Integer> {

	public User findUserByUsername(String username);
	
}
