package com.jgalante.balance.controller;

import java.util.List;

import com.jgalante.balance.entity.Category;
import com.jgalante.balance.entity.Transaction;
import com.jgalante.balance.persistence.TesteDAO;

public class TransactionController extends
		TesteController<Transaction, TesteDAO> {

	private static final long serialVersionUID = 1L;

	public List<Category> findCategoriesByParent(Long idParent) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT object(o) FROM ");
		sb.append(Category.class.getName());
		sb.append(" AS o WHERE o.parent = ");
		sb.append(idParent);
		
		return getDAO().findByJpql(sb.toString());
	}
	
	public List<Category> findCategories() {
		return getDAO().findAll(Category.class,true,"parent.text","text");
	}

}
