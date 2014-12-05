package com.jgalante.balance.view;

import java.util.Calendar;
import java.util.Date;
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
	
	@PostConstruct
	public void init() {	
		Calendar calendar = GregorianCalendar.getInstance();
		category = null;	
		year = calendar.get(Calendar.YEAR);
		listDates();
		
		categories = controller.findCategories();
		handleDateChange();
	}

	private void listDates() {
		years = controller.findYearsWithTransaction();
		List<Date> cmbPeriod = controller.findPeriodWithTransaction(year);
		cmbDates = Util.months(cmbPeriod);
		years = controller.findYearsWithTransaction();
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		
		startDate = (Calendar)((LinkedList<ColumnModel>)cmbDates).getFirst().getValue();//(Util.subtractMonthstoCalendar(calendar, 1).get(Calendar.MONTH)).getValue();
		endDate = (Calendar)((LinkedList<ColumnModel>)cmbDates).getLast().getValue();
		if (endDate.get(Calendar.YEAR) > startDate.get(Calendar.YEAR)) {
			endDate = startDate;
		}
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
	
	public void handleYearChange() {
		listDates();
		handleDateChange();
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
