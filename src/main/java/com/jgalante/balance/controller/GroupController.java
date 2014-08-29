package com.jgalante.balance.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.transaction.Transactional;

import com.jgalante.balance.entity.Category;
import com.jgalante.balance.entity.Group;
import com.jgalante.balance.entity.Transaction;
import com.jgalante.balance.persistence.CategoryDAO;
import com.jgalante.crud.util.ColumnModel;

public class GroupController implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private CategoryDAO categoryDAO;
	
	public LinkedList<ColumnModel> months() {
		return months(0,11);
	}
	
	public LinkedList<ColumnModel> months(Calendar startDate, Calendar endDate) {
		return months(getMonth(startDate), getMonth(endDate));
	}

	public LinkedList<ColumnModel> months(Date startDate, Date endDate) {
    	return months(getMonth(startDate), getMonth(endDate));
	}

	protected LinkedList<ColumnModel> months(int monthStart, int monthEnd) {
		String[] months = new DateFormatSymbols().getMonths();
		LinkedList<ColumnModel> dates = new LinkedList<ColumnModel>();
		
    	boolean visible;
    	if (monthEnd == 0) monthEnd = 11;
    	
	    for (int i = monthStart; i < monthEnd+1; i++) {
	    	visible = false;
	    	if (monthStart <= i && monthEnd >= i) {
	    		visible = true;
	    	}
	    	Calendar date = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), i, 1, 0, 0, 0);
	    	dates.add(new ColumnModel(months[i], date, visible));
	    }
	    return dates;
	}

	protected int getMonth(Date date) {
		if (date == null) {
			return 0;
		}
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return getMonth(calendar);
	}

	protected int getMonth(Calendar calendar) {
		if (calendar == null) {
			return 0;
		}
		int month;
		month = calendar.get(Calendar.MONTH);
		return month;
	}


	public LinkedList<Integer> years() {
		LinkedList<Integer> years = new LinkedList<Integer>();
		Integer current = Calendar.getInstance().get(Calendar.YEAR); 
		for (int i = 0; i < 10; i++,--current) {
			years.add(current);
		}
		return years;
	}
	
	@Transactional
	public List<Group> findGroupsByParent(Long idParent, Calendar startDate, Calendar endDate) {
		int monthEnd = 11;
		List<Category> categories = null;
		Calendar eDate = null;
		if (endDate != null) {
			eDate = (Calendar)endDate.clone();
			eDate.set(Calendar.DAY_OF_MONTH, eDate.getActualMaximum(Calendar.DAY_OF_MONTH));
			monthEnd = getMonth(eDate);
		}
		categories = categoryDAO.findGroupsByParent(idParent, startDate, eDate);
		List<Group> groups = new LinkedList<Group>();
		createGroups(null,categories, groups, getMonth(startDate), monthEnd);
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
	
	
}
