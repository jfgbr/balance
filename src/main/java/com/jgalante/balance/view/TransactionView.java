package com.jgalante.balance.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.jgalante.balance.controller.TransactionController;
import com.jgalante.balance.entity.Category;
import com.jgalante.balance.entity.Transaction;

@Named
@ViewScoped
public class TransactionView extends TesteView<Transaction, TransactionController> {

	private static final long serialVersionUID = 1L;

	private List<Category> categories;
	
	@PostConstruct
	public void init() {
		setEntity(new Transaction());
		setEntities(getController().findAll());
		
		categories = ((TransactionController)getController()).findCategories();
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

}
