package com.oddscorp.internetbanking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oddscorp.internetbanking.dao.RoleDao;
import com.oddscorp.internetbanking.domain.security.Role;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleDao roleDao;
	
	@Override
	public Role findByName(String name) {
		return roleDao.findByName(name);
	}

}
