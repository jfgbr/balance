package com.jgalante.balance.view;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.jgalante.balance.controller.GroupController;
import com.jgalante.balance.entity.Account;
import com.jgalante.balance.entity.Category;
import com.jgalante.balance.entity.Group;
import com.jgalante.crud.util.ColumnModel;
import com.jgalante.crud.util.Util;
import com.jgalante.crud.view.SimpleView;

@Named
@ViewScoped
public class GroupView extends SimpleView {

	private static final long serialVersionUID = 1L;

	@Inject
	private GroupController controller;

	private List<Category> categories;
	
	private List<Group> group;
	
	private List<Integer> years;
	
	private List<ColumnModel> cmbDates;
	
	private List<ColumnModel> dates;

	private Account account;
	
	private Category category;
	
	private Integer year;
	
	private Calendar startDate;
	
	private Calendar endDate;
	
	private Integer totalMonths;

	@PostConstruct
	public void init() {
		category = null;
		cmbDates = Util.months();
		years = Util.years();
		year = years.get(0);
		int currentMonth = GregorianCalendar.getInstance().get(Calendar.MONTH);
		startDate = (Calendar)((LinkedList<ColumnModel>)cmbDates).get(currentMonth-1).getValue();
		endDate = (Calendar)((LinkedList<ColumnModel>)cmbDates).get(currentMonth+1).getValue();
		categories = controller.findCategories();
		handleDateChange();
	}

	public void createListCategories(Category category, Account account, Calendar startDate, Calendar endDate) {
		if (startDate != null) {
			startDate.set(Calendar.YEAR, year);			
		}
		if (endDate != null) {
			endDate.set(Calendar.YEAR, year);			
		}
		Long idParent = null;
		if (category != null) {
			idParent = category.getId();
		}
		Long idAccount = null;
		if (account != null) {
			idAccount = account.getId();
		}
		totalMonths = endDate.get(Calendar.MONTH)-startDate.get(Calendar.MONTH);
		group = controller.findGroupsByParent(idParent, idAccount, startDate, endDate);
	}
	
	public void handleAccountChange() {
//		account = null;
//		try {
//			account = Long.parseLong(event.getNewValue().toString());
//
//		} catch (Exception e) {
//			account = null;
//		}
		createListCategories(category,account,startDate,endDate);
	}

	public void handleCategoryChange() { //ValueChangeEvent event) {
//		category = null;
//		try {
//			category = Long.parseLong(event.getNewValue().toString());
//
//		} catch (Exception e) {
//			category = null;
//		}
		createListCategories(category,account,startDate,endDate);
	}
	
	public void handleDateChange() {
		dates = Util.months(startDate,endDate);
		createListCategories(category,account,startDate,endDate);
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public List<Group> getGroup() {
		return group;
	}

	public List<ColumnModel> getDates() {
		return dates;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Calendar getStartDate() {
		return startDate;
	}

	public void setStartDate(Calendar startDate) {
		if (startDate != null) {
			this.startDate = (Calendar)startDate.clone();
		} else {
			this.startDate = null;
		}
	}

	public Calendar getEndDate() {
		return endDate;
	}

	public void setEndDate(Calendar endDate) {
		if (endDate != null) {
			this.endDate = (Calendar)endDate.clone();
		} else {
			this.endDate = null;
		}
	}

	public List<ColumnModel> getCmbDates() {
		return cmbDates;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public List<Integer> getYears() {
		return years;
	}

	public Integer getTotalMonths() {
		return totalMonths;
	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(Account account) {
		this.account = account;
	}

}
