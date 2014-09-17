package com.jgalante.balance.view;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.jgalante.balance.controller.AccountController;
import com.jgalante.balance.controller.BalanceController;
import com.jgalante.balance.controller.TransferController;
import com.jgalante.balance.entity.Account;
import com.jgalante.balance.entity.Balance;
import com.jgalante.balance.entity.Category;
import com.jgalante.balance.entity.Transaction;
import com.jgalante.crud.util.Filter;
import com.jgalante.crud.util.Filter.Operator;
import com.jgalante.crud.view.CrudView;

@Named
@ViewScoped
public class BalanceView extends CrudView<Balance, BalanceController> {

	private static final long serialVersionUID = 1L;
		
	@Inject
	private AccountController accountController;
	
	@Inject
	private TransferController transferController;
	
	private Account account;
	
	private List<Account> accounts;
	
	private List<Category> categories;
	
	private BigDecimal accountCurrentBalance;

	private BigDecimal currentBalance;

	private Boolean payment;		
	
	@PostConstruct
	public void init() {
		accounts = accountController.findAll();
		currentBalance = accountController.currentBalance(account);
		accountCurrentBalance = getController().currentBalance(account);
		categories = transferController.findTransferCategories();
		payment = false;
	}

	@Override
	public void close() {
		cleanEntity();
		payment = false;
		super.close();
	}
	
	public void pay(Balance balance) {
		payment = true;
//		balance.setTransaction(new Transaction());
//		Balance b1 = (Balance) getDataModel().getRowData();
//		b1.setTransaction(new Transaction());
//		setEntity(b1);
		super.edit();
		getEntity().setTransaction(new Transaction());
		super.list();
	}
	
	public void proceedPayment() {
		getController().pay(getEntity());
		close();
	}
	
	public void handleFilterChange() {
		Filter filterAND = new Filter(Operator.AND, new LinkedList<Filter>());
		filterAND.getFilters().add(new Filter("balanceDate", Calendar.getInstance().getTime(), Operator.EQUAL_LESS));
		getController().setSearchFilter(filterAND);
		
		if (account != null) {
			filterAND.getFilters().add(new Filter("account.id",account.getId()));			
		}
		currentBalance = accountController.currentBalance(account);
		accountCurrentBalance = getController().currentBalance(account);
	}
	
	public BigDecimal getAccountCurrentBalance() {
		return accountCurrentBalance;
	}
	
	public BigDecimal getCurrentBalance() {
		return currentBalance;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public List<Account> getAccounts() {
		return accounts;
	}
	
	public List<Category> getCategories() {
		return categories;
	}

	public Boolean getPayment() {
		return payment;
	}

	public void setPayment(Boolean payment) {
		this.payment = payment;
	}
}
