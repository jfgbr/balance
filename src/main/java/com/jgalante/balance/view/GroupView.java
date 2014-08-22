package com.jgalante.balance.view;

import java.io.Serializable;
import java.util.Calendar;
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

@Named
@ViewScoped
public class GroupView implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private GroupController controller;

	private List<Category> categories;
	private List<Group> group;
	private List<Integer> years;
	private List<ColumnModel> cmbDates;
	private List<ColumnModel> dates;

	private Long category;
	private Integer year;
	private Calendar startDate;
	private Calendar endDate;
	private Integer totalMonths;

	@PostConstruct
	public void init() {
		category = null;
		dates = controller.months();
		cmbDates = dates;
		years = controller.years();
		year = years.get(0);
		startDate = (Calendar)((LinkedList<ColumnModel>)cmbDates).getFirst().getValue();
		endDate = (Calendar)((LinkedList<ColumnModel>)cmbDates).getLast().getValue();
		createListCategories(null, startDate, endDate);
	}

	public void createListCategories(Long idParent, Calendar startDate, Calendar endDate) {
		if (startDate != null) {
			startDate.set(Calendar.YEAR, year);			
		}
		if (endDate != null) {
			endDate.set(Calendar.YEAR, year);			
		}
		totalMonths = endDate.get(Calendar.MONTH)-startDate.get(Calendar.MONTH);
		group = controller.findGroupsByParent(idParent, startDate, endDate);
	}

	public void handleCategoryChange(ValueChangeEvent event) {
		category = null;
		try {
			category = Long.parseLong(event.getNewValue().toString());

		} catch (Exception e) {
			category = null;
		}
		createListCategories(category,startDate,endDate);
	}
	
	public void handleDateChange() {
//		((Calendar)cmbDates.get(0).getValue()).getTime()
		dates = controller.months(startDate,endDate);
		createListCategories(category,startDate,endDate);
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
