package com.oddscorp.internetbanking.service;

import java.security.Principal;

import com.oddscorp.internetbanking.domain.PrimaryAccount;
import com.oddscorp.internetbanking.domain.SavingAccount;

public interface AccountService {

	PrimaryAccount createPrimaryAccount();
	SavingAccount createSavingsAccount();
	void deposit(String accountType, double amount, Principal principal);
	void withdraw(String accountType, double amount, Principal principal); 
}
