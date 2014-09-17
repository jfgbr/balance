package com.jgalante.balance.controller;

import java.io.Serializable;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import com.jgalante.balance.entity.Account;
import com.jgalante.balance.entity.Balance;
import com.jgalante.balance.entity.Category;
import com.jgalante.balance.entity.Compare;
import com.jgalante.balance.persistence.CompareDAO;
import com.jgalante.crud.util.Util;

public class CompareController implements Serializable {
	
	private static final long serialVersionUID = 1L;
	

	@Inject
	private AccountController accountController;
	
	@Inject
	private CategoryController categoryController;
	
	@Inject
	private TransactionController transactionController;
//	
//	@Inject
//	private BalanceController balanceController;
	
	
	@Inject
	private CompareDAO compareDAO;


	public List<Account> findAllAccounts() {
		return accountController.searchAll(null);
	}
	
	public List<Category> findCategories(Long idParent) {
		return categoryController.findCategoryByParent(idParent);
	}

	public List<Compare> findCompares(Account account, Category category,
			Calendar startDate, Calendar endDate) {
//		Balance balance = balanceController.findByAccountCalendar(account, startDate, endDate);
//		BigDecimal valueTransaction = transactionController.currentBalance(account, startDate, endDate);
		
		
		
		return compareDAO.findCompares(account, category, startDate);
	}
	
	
	public List<Compare> findBalanceTransaction(Account account, Calendar startDate, Calendar endDate) {
		
		List<Compare> compares = new LinkedList<Compare>();
		if (account != null) {
			Account accountRet = accountController.findBalanceTransaction(account, startDate, endDate);
			
			for (Balance balance : accountRet.getBalances()) {
				Compare compare = new Compare();
				compare.setAccount(account);
				compare.setDate(balance.getBalanceDate());
				Calendar calendar = Util.convertDateToCalendar(balance.getBalanceDate());
				if ("Credit Card".equals(account.getType().getText())) {
					compare.setEstimateValue(transactionController.currentBalanceCreditCard(account, calendar, calendar));
				} else {
					compare.setEstimateValue(transactionController.currentBalance(account, calendar, calendar));
				}
				compare.setBalanceValue(balance.getSignedValue());
				compares.add(compare);
			}
		}
		
		return compares;
	}
}
