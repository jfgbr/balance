package com.jgalante.balance.view;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.jgalante.balance.controller.GroupController;
import com.jgalante.balance.entity.Category;
import com.jgalante.balance.entity.Group;
import com.jgalante.balance.util.ColumnModel;

@Named
@ViewScoped
public class GroupView implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private GroupController controller;
	
	private List<Category> categories;
	private Long category;
	private List<Group> group;
	private List<ColumnModel> dates;
	
	@PostConstruct
	public void init() {
		category = null;//new Category();
		dates = controller.months();
		
		createListCategories(null);
	}

	public void createListCategories(Long idParent) {
		group = controller.findGroupsByParent(idParent);
	}
	
	public void handleCategoryChange(ValueChangeEvent event) {
		category = null;
		try {
			category = Long.parseLong(event.getNewValue()
					.toString());

			
		} catch (Exception e) {
			category = null;
		}
		createListCategories(category);
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

}
