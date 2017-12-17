package com.oddscorp.internetbanking.service;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oddscorp.internetbanking.dao.PrimaryAccountDao;
import com.oddscorp.internetbanking.dao.SavingsAccountDao;
import com.oddscorp.internetbanking.domain.PrimaryAccount;
import com.oddscorp.internetbanking.domain.PrimaryTransaction;
import com.oddscorp.internetbanking.domain.SavingAccount;
import com.oddscorp.internetbanking.domain.SavingsTransaction;
import com.oddscorp.internetbanking.domain.User;

@Service
public class AccountServiceImpl implements AccountService {

	private static int nextAccountNumber = 11223148;
	
	@Autowired
	private PrimaryAccountDao primaryAccountDao;
	
	@Autowired
	private SavingsAccountDao savingAccountDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TransactionService transactionService;
	
	@Override
	public PrimaryAccount createPrimaryAccount() {
		PrimaryAccount primaryAccount = new PrimaryAccount();
		primaryAccount.setAccountBalance(new BigDecimal(0.0));
		primaryAccount.setAccountNumber(accountGen());
		primaryAccountDao.save(primaryAccount);
		
		return primaryAccountDao.findByAccountNumber(primaryAccount.getAccountNumber());
	}

	private int accountGen() {
		return ++nextAccountNumber;
	}

	@Override
	public SavingAccount createSavingsAccount() {
		SavingAccount savingAccount = new SavingAccount();
		savingAccount.setAccountBalance(new BigDecimal(0.0));
		savingAccount.setAccountNumber(accountGen());
		
		savingAccountDao.save(savingAccount);
		
		return savingAccountDao.findByAccountNumber(savingAccount.getAccountNumber());
	}

	@Override
	public void deposit(String accountType, double amount, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		
		if(accountType.equalsIgnoreCase("Primary")) {
			PrimaryAccount primaryAccount = user.getPrimaryAccount();
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().add(new BigDecimal(amount)));
			primaryAccountDao.save(primaryAccount);
			
			Date date = new Date();
			
			PrimaryTransaction primaryTransaction = new PrimaryTransaction(date,"Deposit to Primary Account",
					"Account", "Finished", amount, primaryAccount.getAccountBalance(),primaryAccount);
			
			transactionService.savePrimaryDepositTransaction(primaryTransaction);
			
		}else if(accountType.equalsIgnoreCase("Savings")){
			SavingAccount savingAccount = user.getSavingAccount();
			savingAccount.setAccountBalance(savingAccount.getAccountBalance().add(new BigDecimal(amount)));
			
			Date date = new Date();
			
			SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Deposit to Savings Account",
					"Account", "Finished", amount, savingAccount.getAccountBalance(), savingAccount);
			
			transactionService.saveSavingsDepositTransaction(savingsTransaction);
		}
	}

	@Override
	public void withdraw(String accountType, double amount, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		
		if(accountType.equalsIgnoreCase("Primary")) {
			PrimaryAccount primaryAccount = user.getPrimaryAccount();
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			primaryAccountDao.save(primaryAccount);
			
			Date date = new Date();
			
			PrimaryTransaction primaryTransaction = new PrimaryTransaction(date,"Withdraw from Primary Account",
					"Account", "Finished", amount, primaryAccount.getAccountBalance(),primaryAccount);
			
			transactionService.savePrimaryWithdrawTransaction(primaryTransaction);
			
		}else if(accountType.equalsIgnoreCase("Savings")){
			SavingAccount savingAccount = user.getSavingAccount();
			savingAccount.setAccountBalance(savingAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			
			Date date = new Date();
			
			SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Withdrwa from Savings Account",
					"Account", "Finished", amount, savingAccount.getAccountBalance(), savingAccount);
			
			transactionService.saveSavingsWithdrawTransaction(savingsTransaction);
		}
	}

}
