package com.jgalante.balance.controller;

import java.math.BigDecimal;

import com.jgalante.balance.entity.Account;
import com.jgalante.balance.persistence.AccountDAO;

public class AccountController extends BaseController<Account, AccountDAO> {

	private static final long serialVersionUID = 1L;
	
	public BigDecimal currentBalance(Account account) {
		return ((AccountDAO)getDAO()).currentBalance(account);
	}
}
