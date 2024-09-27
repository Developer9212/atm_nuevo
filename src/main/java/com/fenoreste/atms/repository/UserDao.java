package com.fenoreste.atms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.fenoreste.atms.entity.User;

import java.util.List;
import java.util.Optional;


public interface UserDao extends JpaRepository<User, Integer> {


	User findByUsername(String username);
    Optional<User> findOneByUsername(String username);
	List<User> findByUsernameContaining(String username);

}
