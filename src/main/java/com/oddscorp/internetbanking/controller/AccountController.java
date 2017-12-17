package com.oddscorp.internetbanking.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.oddscorp.internetbanking.domain.PrimaryAccount;
import com.oddscorp.internetbanking.domain.PrimaryTransaction;
import com.oddscorp.internetbanking.domain.SavingAccount;
import com.oddscorp.internetbanking.domain.SavingsTransaction;
import com.oddscorp.internetbanking.domain.User;
import com.oddscorp.internetbanking.service.AccountService;
import com.oddscorp.internetbanking.service.TransactionService;
import com.oddscorp.internetbanking.service.UserService;

@Controller
@RequestMapping("/account")
public class AccountController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private TransactionService transactionService;
	
	@RequestMapping("/primaryAccount")
	public String primaryAccount(Model model, Principal principal) {
		List<PrimaryTransaction> primaryTransactionList = transactionService.findPrimaryTransactionList(principal.getName());
		
		User user = userService.findByUsername(principal.getName());
		PrimaryAccount primaryAccount = user.getPrimaryAccount();
		
		model.addAttribute("primaryAccount", primaryAccount);
		model.addAttribute("primaryTransactionList", primaryTransactionList);
		
		return "primaryAccount";
	}
	
	@RequestMapping("/savingAccount")
	public String savingAccount(Model model, Principal principal) {
		List<SavingsTransaction> savingsTransactionList = transactionService.findSavingsTransactionList(principal.getName());
		
		User user = userService.findByUsername(principal.getName());
		SavingAccount savingAccount = user.getSavingAccount();
		
		model.addAttribute("savingAccount",savingAccount);
		model.addAttribute("savingsTransactionList",savingsTransactionList);
		
		return "savingAccount";
	}
	
	@RequestMapping(value = "/deposit", method = RequestMethod.GET)
	public String deposit(Model model) {
		model.addAttribute("accountType", "");
		model.addAttribute("amount", "");
		
		return "deposit";
	}
	@RequestMapping(value = "/deposit", method = RequestMethod.POST)
	public String depositPost(@ModelAttribute("amount") String amount,
			@ModelAttribute("accountType") String accountType, Principal principal) {
		accountService.deposit(accountType, Double.parseDouble(amount), principal);
		
		  return "redirect:/userFront";
	}
	
	@RequestMapping(value = "/withdraw", method = RequestMethod.GET)
	public String withdraw(Model model) {
		model.addAttribute("accountType", "");
		model.addAttribute("amount", "");
		
		return "withdraw";
	}
	@RequestMapping(value = "/withdraw", method = RequestMethod.POST)
	public String withdrawPost(@ModelAttribute("amount") String amount,
			@ModelAttribute("accountType") String accountType, Principal principal) {
		accountService.withdraw(accountType, Double.parseDouble(amount), principal);
		
		  return "redirect:/userFront";
	}
}
