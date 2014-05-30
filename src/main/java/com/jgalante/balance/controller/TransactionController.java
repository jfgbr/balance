package com.jgalante.balance.controller;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.hibernate.LazyInitializationException;

import com.jgalante.balance.entity.Category;
import com.jgalante.balance.entity.Group2;
import com.jgalante.balance.entity.Transaction;
import com.jgalante.balance.persistence.TesteDAO;

public class TransactionController extends
		TesteController<Transaction, TesteDAO> {

	private static final long serialVersionUID = 1L;

	@Transactional
	public List<Category> findCategoriesByParent(Long idParent) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT DISTINCT object(o) FROM ");
		sb.append(Category.class.getName());
		sb.append(" AS o ");
		sb.append(" LEFT JOIN FETCH o.parent p ");
		sb.append(" LEFT JOIN FETCH o.subCategories s ");
		sb.append(" LEFT JOIN FETCH o.transactions t ");
		sb.append(" LEFT JOIN FETCH s.transactions t1 ");
//		sb.append(" LEFT JOIN FETCH t.transactionType tp ");
		sb.append(" LEFT JOIN FETCH t.person pe ");
		sb.append(" LEFT JOIN FETCH t1.person pe1 ");
		sb.append(" WHERE p = ");
		sb.append(idParent);
		sb.append(" ORDER BY o.order, o.id, s.id, pe.name, pe1.name ASC");
		
		List<Category> categories = getDAO().findByJpql(sb.toString());
		return categories;
	}
	
//	@Transactional
	public List<Group2> findGroupsByParent(Long idParent) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT object(o) FROM ");
		sb.append(Category.class.getName());
		sb.append(" AS o ");
		sb.append(" LEFT JOIN FETCH o.parent p ");
//		sb.append(" JOIN FETCH o.subCategories s ");
//		sb.append(" JOIN FETCH s.transactions t ");
		sb.append(" WHERE p = ");
		sb.append(idParent);
		sb.append(" ORDER BY o.text, p.text ASC");
		
		List<Category> categories = findCategoriesByParent(idParent);//getDAO().findByJpql(sb.toString());
		List<Group2> groups = new LinkedList<Group2>();
		createGroups(null,categories, groups);
		return groups;
	}

	private BigDecimal createGroups(Group2 root, List<Category> categories, List<Group2> groups) {
		Transaction total = new Transaction();
		for (Category category : categories) {
//			Transaction transaction = null;
			Group2 newRoot = new Group2(category, null);
			Set<Transaction> transactions = category.getTransactions();
			if (transactions != null && !transactions.isEmpty()) {
				for (Transaction item : transactions) {
					Group2 node = new Group2(category, item);
					groups.add(node);
//					node.addValue(item.getTransactionDate(), item.getValue());
					root.addValue(item.getTransactionDate(), item.getValue());
				}
			} else {
				newRoot = new Group2(category, null);
				groups.add(newRoot);
			
				
				List<Category> subCategories;// = new LinkedList<Category>(category.getSubCategories());//new LinkedList<Category>(findCategoriesByParent(category.getId()));
				try {
					subCategories = new LinkedList<Category>(category.getSubCategories());
				} catch (LazyInitializationException e) {
					subCategories = new LinkedList<Category>(findCategoriesByParent(category.getId()));
				}
//				root.addValue(transaction.getTransactionDate(), createGroups(subCategories, groups));
				createGroups(newRoot, subCategories, groups);
			}
			total.addValue(newRoot.getTransaction().getValue());
		}
		return total.getValue();
	}
		
	public List<Category> findCategories() {
		return getDAO().findAll(Category.class,true,"parent.text","text");
	}

}
