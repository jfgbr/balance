package com.jgalante.balance.persistence;

import java.util.List;

import javax.persistence.criteria.CriteriaQuery;

import com.jgalante.balance.entity.Category;
import com.jgalante.balance.entity.Transaction;
import com.jgalante.jgcrud.persistence.BaseDAO;

public class TransactionDAO extends BaseDAO<Transaction> {

	private static final long serialVersionUID = 1L;
	
	public List<Category> findCategories() {
		CriteriaQuery<Category> cq = getEntityManager().getCriteriaBuilder().createQuery(Category.class);
		cq.select(cq.from(Category.class));
		return getEntityManager().createQuery(cq).getResultList();
	}
}
