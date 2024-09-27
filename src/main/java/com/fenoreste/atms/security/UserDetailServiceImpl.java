package com.fenoreste.atms.security;

import com.fenoreste.atms.entity.User;
import com.fenoreste.atms.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      User user = userDao
                .findOneByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("El usuario de nombre:"+username+" No existe en el sistema"));

      return new UserDetailsImpl(user);
    }



}
