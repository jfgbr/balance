package com.jgalante.balance.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.transaction.Transactional;

import com.jgalante.balance.entity.Category;
import com.jgalante.balance.entity.Group;
import com.jgalante.balance.entity.Transaction;
import com.jgalante.balance.persistence.CategoryDAO;
import com.jgalante.balance.util.ColumnModel;

public class GroupController implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private CategoryDAO categoryDAO;

	public LinkedList<ColumnModel> months() {
		LinkedList<ColumnModel> dates = new LinkedList<ColumnModel>();
		
	    String[] months = new DateFormatSymbols().getMonths();
	    for (int i = 0; i < 12; i++) {
	      dates.add(new ColumnModel(months[i]));
	    }
	    return dates;
	}
	
	@Transactional
	public List<Group> findGroupsByParent(Long idParent) {
		List<Category> categories = categoryDAO.findGroupsByParent(idParent);
		List<Group> groups = new LinkedList<Group>();
		createGroups(null,categories, groups);
		return groups;
	}

	private BigDecimal createGroups(Group root, List<Category> categories, List<Group> groups) {
		Transaction total = new Transaction();
		for (Category category : categories) {
			Group newRoot = new Group(category, null);
			Set<Transaction> transactions = category.getTransactions();
			if (transactions != null && !transactions.isEmpty()) {
				Group node = new Group(category, null);
				groups.add(node);
				root.addSubCategories(node);
				for (Transaction item : transactions) {
					node.addValue(item.getTransactionDate(), item.getValue());
					root.addValue(item.getTransactionDate(), item.getValue());
				}
			} else {
				newRoot = new Group(category, null);
				groups.add(newRoot);
			
				if (category.getLevel().equals(1)) {
					createGroups(newRoot, new LinkedList<Category>(category.getSubCategories()), groups);
				}
			}
			total.addValue(newRoot.getTransaction().getValue());
		}
		return total.getValue();
	}
	
}
