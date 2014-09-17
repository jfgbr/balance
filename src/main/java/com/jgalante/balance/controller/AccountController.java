package com.jgalante.balance.controller;

import java.math.BigDecimal;
import java.util.Calendar;

import com.jgalante.balance.entity.Account;
import com.jgalante.balance.persistence.AccountDAO;
import com.jgalante.crud.controller.CrudController;

public class AccountController extends CrudController<Account, AccountDAO> {

	private static final long serialVersionUID = 1L;
	
	public BigDecimal currentBalance(Account account) {
		return ((AccountDAO)getDAO()).currentBalance(account);
	}
	
	public Account findBalanceTransaction(Account account, Calendar startDate, Calendar endDate) {
		return ((AccountDAO)getDAO()).findBalanceTransaction(account, startDate, endDate);
	}
}
