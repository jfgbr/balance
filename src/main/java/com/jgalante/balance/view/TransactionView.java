package com.jgalante.balance.view;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.jgalante.balance.controller.AccountController;
import com.jgalante.balance.controller.CategoryController;
import com.jgalante.balance.controller.TransactionController;
import com.jgalante.balance.entity.Account;
import com.jgalante.balance.entity.Category;
import com.jgalante.balance.entity.Transaction;
import com.jgalante.crud.util.Filter;
import com.jgalante.crud.util.Filter.Operator;
import com.jgalante.crud.util.Util;
import com.jgalante.crud.util.ViewState;
import com.jgalante.crud.view.CrudView;

@Named
@ViewScoped
public class TransactionView extends
		CrudView<Transaction, TransactionController> {

	private static final long serialVersionUID = 1L;

	@Inject
	private CategoryController categoryController;

	private List<Category> categories;

	private List<Category> subCategories;

	private Category category;

	@Inject
	private AccountController accountController;

	private Account account;

	private List<Account> accounts;

	private Integer numMonths = 1;

	private Category subCategory;

	private BigDecimal currentBalance;

	private BigDecimal accountCurrentBalance;

	private List<Transaction> selectedTransactions;

	private Date duplicateDate;
	
	private static ViewState DUPLICATE = new ViewState(99,"Duplicate"); 

	@PostConstruct
	public void init() {
		currentBalance = getController().currentBalance();
		// categories = categoryController.findCategoryByParent(null);
		accounts = accountController.findAll();
		handleCategoryChange();
	}

	public void handleCategoryChange() {
		Filter filterAND = new Filter(Operator.AND, new LinkedList<Filter>());
		filterAND.getFilters().add(
				new Filter("transactionDate", Util.addMonthstoCalendar(Calendar.getInstance(), 2).getTime(),
						Operator.EQUAL_LESS));
		getController().setSearchFilter(filterAND);

		if (account != null) {
			filterAND.getFilters().add(
					new Filter("account.id", account.getId()));
		}

		if (category == null) {
			subCategories = null;
			cleanEntity();
			categories = categoryController.findCategoryByParent(null);
			accountCurrentBalance = getController().currentBalance(account,
					category);
		} else {
			if (getEntity().getCategory() != null
					&& category.getId().equals(
							getEntity().getCategory().getParent().getId())) {
				filterAND.getFilters().add(
						new Filter("category.id", getEntity().getCategory()
								.getId()));
				accountCurrentBalance = getController().currentBalance(account,
						getEntity().getCategory());
			} else {
				cleanEntity();
				filterAND.getFilters().add(
						new Filter("category.parent.id", category.getId()));
				accountCurrentBalance = getController().currentBalance(account,
						category);
			}
			subCategories = categoryController.findCategoryByParent(category
					.getId());
		}

	}

	@Override
	public void newEntity() {
		setViewState(ViewState.NEW);
		getEntity().setTransactionDate(null);
		getEntity().setValue(null);
		getEntity().setText(null);
	}

	@Override
	public void edit() {
		super.edit();
		category = new Category();
		category.setId(getEntity().getCategory().getParent().getId());
		handleCategoryChange();
	}

	@Override
	public Boolean save() {
		if (isDuplicate()) {
			duplicate();
		} else {
			try {
				((TransactionController) getController()).save(getEntity(),
						numMonths);
				subCategory = getEntity().getCategory();
				
				newEntity();
				getEntity().setCategory(subCategory);
				
				numMonths = 1;
				handleCategoryChange();
	
				currentBalance = getController().currentBalance();
	
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_INFO,
								getMessage("save.success"), null));
	
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								getMessage("error"), null));
				return false;
			}
		}

		return true;
	}

	@Override
	public void remove() {
		super.remove();
		currentBalance = getController().currentBalance();
	}

	@Override
	public void close() {
		numMonths = 1;
		// category = null;
		// subCategories = null;
		cleanEntity();
		super.close();
		handleCategoryChange();
	}

	public void duplicate() {
		int numMonths = this.numMonths;
		for (Transaction item : selectedTransactions) {
			copy(item, duplicateDate);
			super.save();
			this.numMonths = numMonths;
		}
		list();
		this.numMonths = 1;
		selectedTransactions = null;
	}
	
	public void copy(Transaction base) {
		getDataModel().reset();
		newEntity();
		copy(base, base.getTransactionDate());
	}
	
	public void copy(Transaction base, Date date) {
		getEntity().setAccount(base.getAccount());
		getEntity().setCategory(base.getCategory());
		getEntity().setTransactionDate(date);
		getEntity().setValue(base.getValue());
		getEntity().setText(base.getText());
		category = new Category();
		category.setId(base.getCategory().getParent().getId());
		// String json = Util.writeJson(Transaction.class, getEntity());
		// setEntity(Util.readJson(Transaction.class,
		// Util.writeJson(Transaction.class, base)));
		// getEntity().setId(null);
		handleCategoryChange();
	}
	
	public void changeViewStateToDuplicate() {
		setViewState(DUPLICATE);
		duplicateDate = Calendar.getInstance().getTime();
	}

	public List<Category> getCategories() {
		return categories;
	}

	public List<Category> getSubCategories() {
		return subCategories;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Category getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(Category subCategory) {
		this.subCategory = subCategory;
	}

	public Integer getNumMonths() {
		return numMonths;
	}

	public void setNumMonths(Integer numMonths) {
		this.numMonths = numMonths;
	}

	public BigDecimal getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(BigDecimal currentBalance) {
		this.currentBalance = currentBalance;
	}

	public BigDecimal getAccountCurrentBalance() {
		return accountCurrentBalance;
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

	/**
	 * @return the selectedTransactions
	 */
	public List<Transaction> getSelectedTransactions() {
		return selectedTransactions;
	}

	/**
	 * @param selectedTransactions
	 *            the selectedTransactions to set
	 */
	public void setSelectedTransactions(List<Transaction> selectedTransactions) {
		this.selectedTransactions = selectedTransactions;
	}

	/**
	 * @return the duplicateDate
	 */
	public Date getDuplicateDate() {
		return duplicateDate;
	}

	/**
	 * @param duplicateDate
	 *            the duplicateDate to set
	 */
	public void setDuplicateDate(Date duplicateDate) {
		this.duplicateDate = duplicateDate;
	}

	public boolean isDuplicate() {
		return DUPLICATE.equals(getViewState());
	}
}
