package com.oddscorp.internetbanking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.oddscorp.internetbanking.dao.UserDao;
import com.oddscorp.internetbanking.domain.User;

@Service
public class UserSecurityService implements UserDetailsService {

	@Autowired
	private UserDao userDao;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userDao.findByUsername(username);
		if(null == user) {
			throw new UsernameNotFoundException("Username "+username+" not found");
		}
		return user;
	}

}
