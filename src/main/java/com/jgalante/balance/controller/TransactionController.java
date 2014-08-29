package com.jgalante.balance.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import com.jgalante.balance.entity.Transaction;
import com.jgalante.balance.persistence.TransactionDAO;
import com.jgalante.crud.controller.CrudController;
import com.jgalante.crud.util.Util;

public class TransactionController extends
		CrudController<Transaction, TransactionDAO> {

	private static final long serialVersionUID = 1L;
	
	@Override
	public List<Transaction> search(int first, int pageSize,
			Map<String, Boolean> sort, Map<String, Object> filters) {
		return super.search(first, pageSize, sort, filters);
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
	
}
