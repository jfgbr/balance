package com.jgalante.balance.controller;

import java.util.List;

import com.jgalante.balance.entity.Category;
import com.jgalante.balance.persistence.CategoryDAO;

public class CategoryController extends BaseController<Category, CategoryDAO> {

	private static final long serialVersionUID = 1L;

	public List<Category> findCategoryByParent(Long idParent) {
		return ((CategoryDAO)getDAO()).findCategoryByParent(idParent);
	}
}
