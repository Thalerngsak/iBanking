package com.oddscorp.internetbanking.service;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oddscorp.internetbanking.dao.PrimaryAccountDao;
import com.oddscorp.internetbanking.dao.PrimaryTransactionDao;
import com.oddscorp.internetbanking.dao.RecipientDao;
import com.oddscorp.internetbanking.dao.SavingsAccountDao;
import com.oddscorp.internetbanking.dao.SavingsTransactionDao;
import com.oddscorp.internetbanking.domain.PrimaryAccount;
import com.oddscorp.internetbanking.domain.PrimaryTransaction;
import com.oddscorp.internetbanking.domain.Recipient;
import com.oddscorp.internetbanking.domain.SavingAccount;
import com.oddscorp.internetbanking.domain.SavingsTransaction;
import com.oddscorp.internetbanking.domain.User;


@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private UserService userService;

	@Autowired
	private PrimaryTransactionDao primaryTransactionDao;

	@Autowired
	private SavingsTransactionDao savingsTransactionDao;
	
	@Autowired
	private PrimaryAccountDao primaryAccountDao;
	
	@Autowired
	private SavingsAccountDao savingsAccountDao;
	
	@Autowired
	private RecipientDao recipientDao;

	@Override
	public List<PrimaryTransaction> findPrimaryTransactionList(String username) {
		User user = userService.findByUsername(username);
		List<PrimaryTransaction> primaryTransactions = user.getPrimaryAccount().getPrimaryTransactionList();
		return primaryTransactions;
	}

	@Override
	public List<SavingsTransaction> findSavingsTransactionList(String username) {
		User user = userService.findByUsername(username);
		List<SavingsTransaction> savingsTransactions = user.getSavingAccount().getSavingTransactionList();
		return savingsTransactions;
	}

	@Override
	public void savePrimaryDepositTransaction(PrimaryTransaction primaryTransaction) {
		primaryTransactionDao.save(primaryTransaction);
	}

	@Override
	public void saveSavingsDepositTransaction(SavingsTransaction savingsTransaction) {
		savingsTransactionDao.save(savingsTransaction);

	}

	@Override
	public void savePrimaryWithdrawTransaction(PrimaryTransaction primaryTransaction) {
		primaryTransactionDao.save(primaryTransaction);
	}

	@Override
	public void saveSavingsWithdrawTransaction(SavingsTransaction savingsTransact) {
		savingsTransactionDao.save(savingsTransact);
	}

	public void betweenAccountsTransfer(String transferFrom, String transferTo, String amount,
			PrimaryAccount primaryAccount, SavingAccount savingsAccount) throws Exception {
		if (transferFrom.equalsIgnoreCase("Primary") && transferTo.equalsIgnoreCase("Savings")) {
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().add(new BigDecimal(amount)));
			primaryAccountDao.save(primaryAccount);
			savingsAccountDao.save(savingsAccount);

			Date date = new Date();

			PrimaryTransaction primaryTransaction = new PrimaryTransaction(date,
					"Between account transfer from " + transferFrom + " to " + transferTo, "Account", "Finished",
					Double.parseDouble(amount), primaryAccount.getAccountBalance(), primaryAccount);
			primaryTransactionDao.save(primaryTransaction);
		} else if (transferFrom.equalsIgnoreCase("Savings") && transferTo.equalsIgnoreCase("Primary")) {
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().add(new BigDecimal(amount)));
			savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			primaryAccountDao.save(primaryAccount);
			savingsAccountDao.save(savingsAccount);

			Date date = new Date();

			SavingsTransaction savingsTransaction = new SavingsTransaction(date,
					"Between account transfer from " + transferFrom + " to " + transferTo, "Transfer", "Finished",
					Double.parseDouble(amount), savingsAccount.getAccountBalance(), savingsAccount);
			savingsTransactionDao.save(savingsTransaction);
		} else {
			throw new Exception("Invalid Transfer");
		}
	}

	public List<Recipient> findRecipientList(Principal principal) {
		String username = principal.getName();
		List<Recipient> recipientList = recipientDao.findAll().stream() // convert list to stream
				.filter(recipient -> username.equals(recipient.getUser().getUsername())) // filters the line, equals to
																							// username
				.collect(Collectors.toList());

		return recipientList;
	}

	public Recipient saveRecipient(Recipient recipient) {
		return recipientDao.save(recipient);
	}

	public Recipient findRecipientByName(String recipientName) {
		return recipientDao.findByName(recipientName);
	}

	public void deleteRecipientByName(String recipientName) {
		recipientDao.deleteByName(recipientName);
	}
	
	public void toSomeoneElseTransfer(Recipient recipient, String accountType, String amount,
			PrimaryAccount primaryAccount, SavingAccount savingAccount) {
		if (accountType.equalsIgnoreCase("Primary")) {
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			primaryAccountDao.save(primaryAccount);

			Date date = new Date();

			PrimaryTransaction primaryTransaction = new PrimaryTransaction(date,
					"Transfer to recipient " + recipient.getName(), "Transfer", "Finished", Double.parseDouble(amount),
					primaryAccount.getAccountBalance(), primaryAccount);
			primaryTransactionDao.save(primaryTransaction);
		} else if (accountType.equalsIgnoreCase("Savings")) {
			savingAccount.setAccountBalance(savingAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			savingsAccountDao.save(savingAccount);

			Date date = new Date();

			SavingsTransaction savingsTransaction = new SavingsTransaction(date,
					"Transfer to recipient " + recipient.getName(), "Transfer", "Finished", Double.parseDouble(amount),
					savingAccount.getAccountBalance(), savingAccount);
			savingsTransactionDao.save(savingsTransaction);
		}
    }
}
