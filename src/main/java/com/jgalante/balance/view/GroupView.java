package com.jgalante.balance.view;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
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
	private Category category;
	private List<Group> group;
	private List<ColumnModel> dates;
	
	@PostConstruct
	public void init() {
		category = new Category();
		dates = controller.months();
		
		createListCategories(null,null);
	}

	public void createListCategories(List<Group> root, Long idParent) {
		group = controller.findGroupsByParent(idParent);
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

}
