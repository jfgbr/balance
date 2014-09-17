package com.jgalante.balance.controller;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import com.jgalante.balance.entity.Account;
import com.jgalante.balance.entity.Category;
import com.jgalante.balance.entity.Transaction;
import com.jgalante.balance.persistence.TransactionDAO;
import com.jgalante.crud.controller.CrudController;
import com.jgalante.crud.util.Filter;
import com.jgalante.crud.util.Util;

public class TransactionController extends
		CrudController<Transaction, TransactionDAO> {

	private static final long serialVersionUID = 1L;
	
	private Filter searchFilter;
	
	@Override
	public List<Transaction> search(int first, int pageSize,
			Map<String, Boolean> sort, Map<String, Object> filters) {
		cleanFilter();
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
		return getDAO().currentBalance();
	}
	
	public BigDecimal currentBalance(Account account, Category category) {
		return getDAO().currentBalance(account, category);
	}

	public BigDecimal currentBalance(Account account, Calendar startDate, Calendar endDate) {
		return getDAO().currentBalance(account, startDate, endDate);
	}

	public BigDecimal currentBalanceCreditCard(Account account, Calendar startDate, Calendar endDate) {
		return getDAO().currentBalanceCreditCard(account, startDate, endDate);
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
