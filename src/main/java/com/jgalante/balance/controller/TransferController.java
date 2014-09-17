package com.jgalante.balance.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import com.jgalante.balance.entity.Account;
import com.jgalante.balance.entity.Category;
import com.jgalante.balance.entity.Transaction;

public class TransferController implements Serializable {
	
	private static final long serialVersionUID = 1L;
	

	@Inject
	private AccountController accountController;
	
	@Inject
	private CategoryController categoryController;
	
	@Inject
	private TransactionController transactionController;


	public List<Account> findAllAccounts() {
		return accountController.findAll();
	}
	

	@Transactional(rollbackOn = Exception.class)
	public Transaction transfer(Account accountFrom, Account accountTo, Category categoryFrom, Category categoryTo, Date transferDate, BigDecimal value) {
		Transaction transactionFrom = new Transaction();
		Transaction transactionTo = new Transaction();
		
		transactionFrom.setAccount(accountFrom);
		transactionFrom.setCategory(categoryController.findTransferFromParent(categoryFrom.getId(), false));
		transactionFrom.setTransactionDate(transferDate);
		transactionFrom.setValue(value);
		transactionFrom.setText("Transfer");
		
		transactionTo.setAccount(accountTo);
		transactionTo.setCategory(categoryController.findTransferFromParent(categoryTo.getId(), true));
		transactionTo.setTransactionDate(transferDate);
		transactionTo.setValue(value);
		transactionTo.setText("Transfer");
		
		transactionController.save(transactionFrom);
		return transactionController.save(transactionTo);
	}


	public List<Category> findTransferCategories() {
		return categoryController.findTransferCategories();
	}
}
