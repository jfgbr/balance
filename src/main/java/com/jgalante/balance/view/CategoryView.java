package com.jgalante.balance.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.jgalante.balance.controller.CategoryController;
import com.jgalante.balance.entity.Category;

@Named
@ViewScoped
public class CategoryView extends BaseView<Category, CategoryController> {

	private static final long serialVersionUID = 3L;
	private List<Category> categories;

	@PostConstruct
	public void init() {
		categories = ((CategoryController) getController())
				.findCategoryByParent(null);
	}

	public List<Category> getCategories() {
		return categories;
	}

}
