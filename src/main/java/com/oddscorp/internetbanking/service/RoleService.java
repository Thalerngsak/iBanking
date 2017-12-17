package com.oddscorp.internetbanking.service;

import com.oddscorp.internetbanking.domain.security.Role;

public interface RoleService {
    Role findByName(String name);
}
