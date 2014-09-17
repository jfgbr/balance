package com.jgalante.balance.controller;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import com.jgalante.balance.entity.Account;
import com.jgalante.balance.entity.AccountType;
import com.jgalante.balance.entity.Balance;
import com.jgalante.balance.entity.Category;
import com.jgalante.balance.entity.Person;
import com.jgalante.balance.entity.Transaction;
import com.jgalante.balance.facade.IDAO;
import com.jgalante.balance.persistence.AccountDAO;
import com.jgalante.balance.persistence.BalanceDAO;
import com.jgalante.balance.persistence.CategoryDAO;
import com.jgalante.balance.persistence.TransactionDAO;
import com.jgalante.balance.qualifier.DAO;

public class ExportImportController implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	@DAO(entityClass=Person.class)
	private IDAO personDAO;
	
	@Inject
	@DAO(entityClass=AccountType.class)
	private IDAO accountTypeDAO;
	
	@Inject
	private AccountDAO accountDAO;
	
	@Inject
	private CategoryDAO categoryDAO;
	
	@Inject
	private TransactionDAO transactionDAO;

	@Inject
	private BalanceDAO balanceDAO;
	
	public List<Person> findAllPeople() {
		return personDAO.findAll(Person.class, true);
	}
	
	public List<AccountType> findAllAccountsType() {
		return accountTypeDAO.findAll(AccountType.class, true);
	}
	
	public List<Account> findAllAccounts() {
		return accountDAO.searchAll(null);
	}

	public List<Category> findAllCategories() {
		return categoryDAO.searchAll(null);
	}
	
	public List<Transaction> findAllTransactions() {
		return transactionDAO.searchAll(null);
	}

	@Transactional
	public void save(List<Person> people, List<AccountType> accountsType, List<Account> accounts,
			List<Category> categories, List<Transaction> transactions, List<Balance> balances) {
		for (Person person : people) {
			personDAO.save(person);
		}
		
		for (AccountType accountType : accountsType) {
			accountTypeDAO.save(accountType);
		}
		
		for (Account account : accounts) {
			accountDAO.save(account);
		}
		
		for (Category category : categories) {
			categoryDAO.save(category);
		}
		
		for (Transaction transaction : transactions) {
			transactionDAO.save(transaction);
		}
		
		for (Balance balance : balances) {
			transactionDAO.save(balance);
		}
	}

	public List<Balance> findAllBalances() {
		return balanceDAO.searchAll(null);		
	}
}
