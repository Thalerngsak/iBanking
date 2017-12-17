package com.oddscorp.internetbanking.dao;

import org.springframework.data.repository.CrudRepository;

import com.oddscorp.internetbanking.domain.security.Role;

public interface RoleDao extends CrudRepository<Role, Integer> {
    Role findByName(String name);
}
