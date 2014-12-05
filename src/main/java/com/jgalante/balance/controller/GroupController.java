package com.jgalante.balance.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.transaction.Transactional;

import com.jgalante.balance.entity.Category;
import com.jgalante.balance.entity.Group;
import com.jgalante.balance.entity.Transaction;
import com.jgalante.balance.persistence.CategoryDAO;
import com.jgalante.crud.util.Util;

public class GroupController implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private CategoryDAO categoryDAO;
	
	@Inject
	private TransactionController transactionController;
	
	public List<Category> findCategories() {
		return categoryDAO.findCategoryByParent(null);
	}
	
	@Transactional
	public List<Group> findGroupsByParent(Long idParent, Long idAccount, Calendar startDate, Calendar endDate) {
		int monthEnd = 11;
		List<Category> categories = null;
		Calendar eDate = null;
		if (endDate != null) {
			if (endDate.get(Calendar.YEAR) > startDate.get(Calendar.YEAR)) {
				eDate = Util.endOfMonth(startDate);
				monthEnd = Util.getMonth(Util.endOfMonth(eDate));
			} else {
				eDate = Util.endOfMonth(endDate);
				monthEnd = Util.getMonth(Util.endOfMonth(eDate));
			}
		}
		categories = categoryDAO.findGroupsByParent(idParent, idAccount, startDate, eDate);
		List<Group> groups = new LinkedList<Group>();
		createGroups(null,categories, groups, Util.getMonth(startDate), monthEnd);
		return groups;
	}

	private BigDecimal createGroups(Group root, List<Category> categories, List<Group> groups, int monthStart, int monthEnd) {
		Transaction total = new Transaction();
		for (Category category : categories) {
			Group newRoot = new Group(category, null);
			Set<Transaction> transactions = category.getTransactions();
			if (transactions != null && !transactions.isEmpty()) {
				Group node = new Group(category, null);
				groups.add(node);
				root.addSubCategories(node);
				for (Transaction item : transactions) {
					node.addValue(item.getTransactionDate(), item.getValue(), monthStart, monthEnd, category.getPositive());
					root.addValue(item.getTransactionDate(), item.getValue(), monthStart, monthEnd, category.getPositive());
				}
				node.setTotalValue(categoryDAO.currentBalance(category));
			} else {
				newRoot = new Group(category, null);
				groups.add(newRoot);
				newRoot.setTotalValue(categoryDAO.currentBalance(category));
				if (category.getLevel().equals(1)) {
					createGroups(newRoot, new LinkedList<Category>(category.getSubCategories()), groups, monthStart, monthEnd);
				}
			}
			total.addValue(newRoot.getTransaction().getValue());
		}
		
		return total.getValue();
	}
	
	public List<Date> findPeriodWithTransaction(Integer year) {
		return transactionController.findPeriodWithTransaction(year);
	}
	
	public List<Integer> findYearsWithTransaction() {
		return transactionController.findYearsWithTransaction();
	}
}
