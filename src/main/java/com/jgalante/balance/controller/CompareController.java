package com.jgalante.balance.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import com.jgalante.balance.entity.Account;
import com.jgalante.balance.entity.AccountType;
import com.jgalante.balance.entity.Balance;
import com.jgalante.balance.entity.Category;
import com.jgalante.balance.entity.Compare;
import com.jgalante.crud.util.Util;

public class CompareController implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private AccountController accountController;
	
	@Inject
	private CategoryController categoryController;
	
	@Inject
	private TransactionController transactionController;

	public List<Account> findAllAccounts() {
		return accountController.searchAll(null);
	}
	
	public List<Category> findCategories(Long idParent) {
		return categoryController.findCategoryByParent(idParent);
	}
	
	public List<Compare> findBalanceTransaction(Account account, Calendar startDate, Calendar endDate) {
		
		List<Compare> compares = new LinkedList<Compare>();
		if (account != null) {
			Calendar sDate = Util.subtractMonthstoCalendar(startDate, 1);
			Account accountRet = accountController.findBalanceTransaction(account, sDate, endDate);
			if (accountRet != null) {
				BigDecimal previousDifferenceValue = BigDecimal.ZERO;
				BigDecimal estimateValue = BigDecimal.ZERO;
				Compare compare;
				for (Balance balance : accountRet.getBalances()) {
					compare = new Compare();
					compare.setAccount(account);
					compare.setDate(balance.getBalanceDate());
					Calendar calendar = Util.convertDateToCalendar(balance.getBalanceDate());
					if (AccountType.CREDIT_CARD.equals(account.getType())) {
						estimateValue = transactionController.periodBalanceForCreditCard(account, null, calendar, calendar);
					} else {
						estimateValue = transactionController.periodBalance(account, calendar);
						if (estimateValue != null) {
							if (previousDifferenceValue.compareTo(BigDecimal.ZERO) == -1) {
								estimateValue = estimateValue.subtract(previousDifferenceValue.abs());
							} else {
								if (estimateValue.compareTo(BigDecimal.ZERO) == 1) {
									estimateValue = estimateValue.add(previousDifferenceValue);
								} else { 
									estimateValue = estimateValue.subtract(previousDifferenceValue);
								}
							}
						}
					}
					if (estimateValue != null) {
						compare.setEstimateValue(estimateValue);
						compare.setBalanceValue(balance.getSignedValue());
						previousDifferenceValue = previousDifferenceValue.add(compare.getDifferenceValue());
						compares.add(compare);
					}
				}
			}
		}
		
		return compares;
	}
}
