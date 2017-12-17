package com.oddscorp.internetbanking.dao;

import org.springframework.data.repository.CrudRepository;

import com.oddscorp.internetbanking.domain.PrimaryAccount;

public interface PrimaryAccountDao extends CrudRepository<PrimaryAccount, Long> {
	 PrimaryAccount findByAccountNumber (int accountNumber);
}
