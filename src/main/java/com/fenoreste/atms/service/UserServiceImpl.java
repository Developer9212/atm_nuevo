package com.fenoreste.atms.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fenoreste.atms.entity.User;
import com.fenoreste.atms.repository.UserDao;


@Service
public class UserServiceImpl implements IUserService{
    
	@Autowired
	private UserDao userRepository;

	@Override
	@Transactional(readOnly = true)
	public List<User> findAll() {
    	return (List<User>)userRepository.findAll();
	}

	@Override
	public User save(User user) {

		 User newUser = new User();
         if(userRepository.findByUsernameContaining(user.getUsername()).size() > 0){
			 newUser.setUsername("YA EXISTE");
		 }else{
			 newUser = userRepository.save(user);
		 }
		return newUser;

	}

	@Override
	@Transactional(readOnly = true)
	public User findById(Integer id) {
		return userRepository.findById(id).orElse(null);
	}


	

}
