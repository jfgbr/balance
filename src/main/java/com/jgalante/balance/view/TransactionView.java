package com.jgalante.balance.view;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.jgalante.balance.controller.TransactionController;
import com.jgalante.balance.entity.Category;
import com.jgalante.balance.entity.Transaction;
import com.jgalante.crud.util.Filter;
import com.jgalante.crud.util.ViewState;
import com.jgalante.crud.view.CrudView;

@Named
@ViewScoped
public class TransactionView extends CrudView<Transaction, TransactionController> {

	private static final long serialVersionUID = 1L;
	private List<Category> subCategories;
	
	private Category category;
	
	private Integer numMonths = 1;
	
	private Category subCategory;
	
	private BigDecimal currentBalance;
	
	@PostConstruct
	public void init() {
		currentBalance = getController().currentBalance();
	}

	public void handleCategoryChange() {

		getController().cleanFilter();
		if (category == null) {
			subCategories = null;
			cleanEntity();
		} else {
			if (getEntity().getCategory() != null && category.getId().equals(getEntity().getCategory().getParent().getId())) {
				getController().addFilter(new Filter("category.id",getEntity().getCategory().getId()));
			} else {
				cleanEntity();
				getController().addFilter(new Filter("category.parent.id",category.getId()));
			}
			
			subCategories = getController().createSubCategories(category.getId());
		}
	}
	
	@Override
	public void newEntity() {
		setViewState(ViewState.NEW);
	}
	
	@Override
	public Boolean save() {
		try {
			((TransactionController)getController()).save(getEntity(),numMonths);
			category = null;
			subCategories = null;
			newEntity();
			handleCategoryChange();
			
			currentBalance = getController().currentBalance();
			
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							getMessage("save.success"), null));

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							getMessage("error"), null));
			return false;
		}

		return true;
	}
	
	@Override
	public void remove() {
		super.remove();
		currentBalance = getController().currentBalance();
	}
	
//	public void handleCategoryChange(ValueChangeEvent event) {
//		Long idParent = null;
//		try {
//			idParent = Long.parseLong(event.getNewValue()
//					.toString());
//
//			subCategories = ((TransactionController) getController()).createSubCategories(idParent);
//		} catch (Exception e) {
//			idParent = null;
//		}
//	}

	public List<Category> getSubCategories() {
		return subCategories;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
	public Category getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(Category subCategory) {
		this.subCategory = subCategory;
	}

	public Integer getNumMonths() {
		return numMonths;
	}

	public void setNumMonths(Integer numMonths) {
		this.numMonths = numMonths;
	}

	public BigDecimal getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(BigDecimal currentBalance) {
		this.currentBalance = currentBalance;
	}

}
