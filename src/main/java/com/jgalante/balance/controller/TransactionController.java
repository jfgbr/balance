package com.jgalante.balance.controller;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import com.jgalante.balance.entity.Account;
import com.jgalante.balance.entity.Category;
import com.jgalante.balance.entity.Transaction;
import com.jgalante.balance.persistence.TransactionDAO;
import com.jgalante.crud.controller.CrudController;
import com.jgalante.crud.util.Filter;
import com.jgalante.crud.util.Filter.Operator;
import com.jgalante.crud.util.Util;

public class TransactionController extends
		CrudController<Transaction, TransactionDAO> {

	private static final long serialVersionUID = 1L;
	
	private Filter searchFilter;
	
	public void configureSearch(Account account, Category category, Category parent, Calendar startDate, Calendar endDate) {
		Filter filterAND = new Filter(Operator.AND, new LinkedList<Filter>());
		filterAND.getFilters().add(
				new Filter("transactionDate", Util.beginOfMonth(startDate).getTime(),
						Operator.EQUAL_GREATER));
		filterAND.getFilters().add(
				new Filter("transactionDate", Util.endOfMonth(endDate).getTime(),
						Operator.EQUAL_LESS));
		
		setSearchFilter(filterAND);

		if (account != null) {
			filterAND.getFilters().add(
					new Filter("account.id", account.getId()));
		}
		
		if (category != null) {
			filterAND.getFilters().add(
				new Filter("category.id", category.getId()));
		}
		
		if (parent != null) {
			filterAND.getFilters().add(
				new Filter("category.parent.id", parent.getId()));
		}
	}
	
	@Override
	public List<Transaction> search(int first, int pageSize,
			Map<String, Boolean> sort, Map<String, Object> filters) {
		reset();
		addFilter(searchFilter);
		Map<String, Boolean> tmpSort = new LinkedHashMap<String, Boolean>();
		tmpSort.put("transactionDate", true);
		if (sort != null) {
			tmpSort.putAll(sort);			
		}
		tmpSort.put("id", true);
		return super.search(first, pageSize, tmpSort, filters);
	}

	@Transactional
	public void save(Transaction transaction, Integer numMonths) {
		Date tmpDate = transaction.getTransactionDate();
		for (int i = 0; i < numMonths; i++) {
			super.save(transaction);
			transaction.setTransactionDate(Util.addMonthstoDate(transaction.getTransactionDate(), 1));
		}
		transaction.setTransactionDate(tmpDate);
	}
	
//	public List<Category> createSubCategories(Long idParent) {
//		return categoryDAO.findCategoryByParent(idParent);
//	}
	
	public BigDecimal currentBalance() {
		return currentBalance(null, null);
	}
	
	public BigDecimal currentBalance(Account account, Category category) {
		return getDAO().currentBalance(account, category);
	}

	public BigDecimal periodBalance(Account account, Calendar endDate) {
		return getDAO().periodBalance(account, endDate);
	}
	
	public BigDecimal periodBalance(Account account, Category category, Calendar startDate, Calendar endDate) {
		return getDAO().periodBalance(account, category, startDate, endDate);
	}

	public BigDecimal periodBalanceForCreditCard(Account account, Calendar startDate, Calendar endDate) {
		return getDAO().periodBalanceForCreditCard(account, startDate, endDate);
	}
	
	public Filter getSearchFilter() {
		return searchFilter;
	}

	public void setSearchFilter(Filter searchFilter) {
		this.searchFilter = searchFilter;
	}
	
	@Override
	public Class<Transaction> getEntityClass() {
		return Transaction.class;
	}

}
