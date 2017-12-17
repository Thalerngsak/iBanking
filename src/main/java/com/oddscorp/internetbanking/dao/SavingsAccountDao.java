package com.oddscorp.internetbanking.dao;

import org.springframework.data.repository.CrudRepository;

import com.oddscorp.internetbanking.domain.SavingAccount;


public interface SavingsAccountDao extends CrudRepository<SavingAccount, Long> {
    SavingAccount findByAccountNumber (int accountNumber);
}
