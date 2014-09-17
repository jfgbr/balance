package com.jgalante.balance.controller;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.transaction.Transactional;

import com.jgalante.balance.entity.Account;
import com.jgalante.balance.entity.Balance;
import com.jgalante.balance.persistence.BalanceDAO;
import com.jgalante.crud.controller.CrudController;
import com.jgalante.crud.util.Filter;

public class BalanceController extends
		CrudController<Balance, BalanceDAO> {

	private static final long serialVersionUID = 1L;
	
	private Filter searchFilter;

//	@Inject
//	private CategoryController categoryController;
	
	@Inject
	private TransferController transferController;
	
	@Override
	public List<Balance> search(int first, int pageSize,
			Map<String, Boolean> sort, Map<String, Object> filters) {
		cleanFilter();
		addFilter(searchFilter);
		getDAO().getQueryParam().addJoinFilter("account");
		Map<String, Boolean> tmpSort = new LinkedHashMap<String, Boolean>();
		tmpSort.put("balanceDate", true);
		if (sort != null) {
			tmpSort.putAll(sort);			
		}
		tmpSort.put("id", true);
		return super.search(first, pageSize, tmpSort, filters);
	}
	
	public BigDecimal currentBalance(Account account) {
//		if (account != null) {
			return getDAO().currentBalance(account);
//		}
//		return null;
	}

	public Filter getSearchFilter() {
		return searchFilter;
	}

	public void setSearchFilter(Filter searchFilter) {
		this.searchFilter = searchFilter;
	}

	@Transactional
	public void pay(Balance entity) {
		entity.setTransaction(transferController.transfer(entity.getTransaction().getAccount(),
				entity.getAccount(), 
				entity.getTransaction().getCategory(), entity.getTransaction().getCategory(),
				entity.getBalanceDate(), entity.getValue()));
//		Transaction transaction = entity.getTransaction();
//		transaction.setValue(entity.getValue());
//		transaction.setTransactionDate(entity.getBalanceDate());
//		transaction.setCategory(categoryController.findTransferFromParent(transaction.getCategory().getId(), entity.getPositive()));
//		transaction.setText("Payment");
//		entity.setTransaction(transferController.save(transaction));
		super.save(entity);
	}

	public Balance findByAccountCalendar(Account account, Calendar calendar, Calendar endDate) {
		// TODO Auto-generated method stub
		return null;
	}
}
