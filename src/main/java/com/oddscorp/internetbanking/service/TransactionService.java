package com.oddscorp.internetbanking.service;

import java.security.Principal;
import java.util.List;

import com.oddscorp.internetbanking.domain.PrimaryAccount;
import com.oddscorp.internetbanking.domain.PrimaryTransaction;
import com.oddscorp.internetbanking.domain.Recipient;
import com.oddscorp.internetbanking.domain.SavingAccount;
import com.oddscorp.internetbanking.domain.SavingsTransaction;

public interface TransactionService {
	List<PrimaryTransaction> findPrimaryTransactionList(String username);

    List<SavingsTransaction> findSavingsTransactionList(String username);

    void savePrimaryDepositTransaction(PrimaryTransaction primaryTransaction);

    void saveSavingsDepositTransaction(SavingsTransaction savingsTransaction);
    
    void savePrimaryWithdrawTransaction(PrimaryTransaction primaryTransaction);
    
    void saveSavingsWithdrawTransaction(SavingsTransaction savingsTransact);
    
    void betweenAccountsTransfer(String transferFrom, String transferTo, String amount, PrimaryAccount primaryAccount, SavingAccount savingsAccount) throws Exception;
    
    List<Recipient> findRecipientList(Principal principal);

    Recipient saveRecipient(Recipient recipient);

    Recipient findRecipientByName(String recipientName);

    void deleteRecipientByName(String recipientName);
    
    void toSomeoneElseTransfer(Recipient recipient, String accountType, String amount, PrimaryAccount primaryAccount, SavingAccount savingAccount);
}
