package com.jgalante.balance.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.jgalante.balance.controller.AccountController;
import com.jgalante.balance.entity.Account;

@Named
@ViewScoped
public class AccountView extends BaseView<Account, AccountController> {

	private static final long serialVersionUID = 3L;
	private List<Account> accounts;

	@PostConstruct
	public void init() {
		accounts = getController().findAll();
	}

	public List<Account> getAccounts() {
		return accounts;
	}


}
