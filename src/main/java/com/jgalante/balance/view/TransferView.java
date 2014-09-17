package com.jgalante.balance.view;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.jgalante.balance.controller.TransferController;
import com.jgalante.balance.entity.Account;
import com.jgalante.balance.entity.Category;
import com.jgalante.crud.view.SimpleView;

@Named
@ViewScoped
public class TransferView extends SimpleView {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private TransferController transferController;
	
	private Account accountFrom;
	
	private Account accountTo;
	
	private Category categoryFrom;
	
	private Category categoryTo;
	
	private Date transferDate;
	
	private BigDecimal value;
	
	private List<Account> accounts;
	
	private List<Category> categories;
	
	@PostConstruct
	public void init() {
		accounts = transferController.findAllAccounts();
		categories = transferController.findTransferCategories();
	}
	
	public void transfer() {
		try {
			transferController.transfer(accountFrom, accountTo, categoryFrom, categoryTo, transferDate, value);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							getMessage("save.success"), null));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							getMessage("error"), null));			
		}
	}

	public Account getAccountFrom() {
		return accountFrom;
	}

	public void setAccountFrom(Account accountFrom) {
		this.accountFrom = accountFrom;
	}

	public Account getAccountTo() {
		return accountTo;
	}

	public void setAccountTo(Account accountTo) {
		this.accountTo = accountTo;
	}
	
	public Category getCategoryFrom() {
		return categoryFrom;
	}

	public void setCategoryFrom(Category categoryFrom) {
		this.categoryFrom = categoryFrom;
	}

	public Category getCategoryTo() {
		return categoryTo;
	}

	public void setCategoryTo(Category categoryTo) {
		this.categoryTo = categoryTo;
	}

	public Date getTransferDate() {
		return transferDate;
	}

	public void setTransferDate(Date transferDate) {
		this.transferDate = transferDate;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}
	
	public List<Account> getAccounts() {
		return accounts;
	}

	public List<Category> getCategories() {
		return categories;
	}
	
}
