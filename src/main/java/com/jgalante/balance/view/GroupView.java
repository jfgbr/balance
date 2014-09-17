package com.jgalante.balance.view;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.jgalante.balance.controller.GroupController;
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

	private Long account;
	
	private Long category;
	
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
		handleDateChange();
	}

	public void createListCategories(Long idParent, Long idAccount, Calendar startDate, Calendar endDate) {
		if (startDate != null) {
			startDate.set(Calendar.YEAR, year);			
		}
		if (endDate != null) {
			endDate.set(Calendar.YEAR, year);			
		}
		totalMonths = endDate.get(Calendar.MONTH)-startDate.get(Calendar.MONTH);
		group = controller.findGroupsByParent(idParent, idAccount, startDate, endDate);
	}
	
	public void handleAccountChange(ValueChangeEvent event) {
		account = null;
		try {
			account = Long.parseLong(event.getNewValue().toString());

		} catch (Exception e) {
			account = null;
		}
		createListCategories(category,account,startDate,endDate);
	}

	public void handleCategoryChange(ValueChangeEvent event) {
		category = null;
		try {
			category = Long.parseLong(event.getNewValue().toString());

		} catch (Exception e) {
			category = null;
		}
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

	public Long getCategory() {
		return category;
	}

	public void setCategory(Long category) {
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

	
}
