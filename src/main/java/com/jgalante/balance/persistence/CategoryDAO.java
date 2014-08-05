package com.jgalante.balance.persistence;

import java.util.List;

import javax.transaction.Transactional;

import com.jgalante.balance.entity.Category;

public class CategoryDAO extends BaseDAO{

	private static final long serialVersionUID = 1L;

	
	public List<Category> findCategoryByParent(Long idParent) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT DISTINCT object(o) FROM ");
		sb.append(Category.class.getName());
		sb.append(" AS o ");
		sb.append(" WHERE o.parent.id = ");
		sb.append(idParent);
		sb.append(" ORDER BY o.order, o.text ASC");
		
		List<Category> categories = findByJpql(sb.toString());
		return categories;
	}
	

	@Transactional
	public List<Category> findGroupsByParent(Long idParent) {
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT DISTINCT object(c) FROM ");
		sb.append(Category.class.getName());
		sb.append(" AS c ");
		sb.append(" JOIN FETCH c.subCategories s ");
		sb.append(" LEFT JOIN FETCH s.transactions t ");
		sb.append(" WHERE c.parent.id = ");
		sb.append(idParent);
		sb.append(" GROUP BY YEAR(t.transactionDate), MONTH(t.transactionDate), c.id, s.id");
		sb.append(" ORDER BY c.order, c.text, s.order, s.text, YEAR(t.transactionDate), MONTH(t.transactionDate)");
		
		List<Category> categories = findByJpql(sb.toString());
		return categories;
	}
}
