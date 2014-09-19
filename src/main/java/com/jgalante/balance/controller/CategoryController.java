package com.jgalante.balance.controller;

import java.util.List;

import com.jgalante.balance.entity.Category;
import com.jgalante.balance.persistence.CategoryDAO;

public class CategoryController extends BaseController<Category, CategoryDAO> {

	private static final long serialVersionUID = 1L;

	public List<Category> findCategoryByParent(Long idParent) {
		return ((CategoryDAO)getDAO()).findCategoryByParent(idParent);
	}
	
	public List<Category> findCategoryByParent(Category parent) {
		Long idParent = null;
		if (parent != null) {
			idParent = parent.getId();
		}
		return ((CategoryDAO)getDAO()).findCategoryByParent(idParent);
	}
	

	public Category findParentByCategory(Long id) {
		return ((CategoryDAO)getDAO()).findParentByCategory(id);
	}


	public List<Category> findTransferCategories() {
		return ((CategoryDAO)getDAO()).findTransferCategories();
	}


	public Category findTransferFromParent(Long id, Boolean positive) {
		return ((CategoryDAO)getDAO()).findTransferFromParent(id, positive);
	}
	
	@Override
	public Class<Category> getEntityClass() {
		return Category.class;
	}
}
