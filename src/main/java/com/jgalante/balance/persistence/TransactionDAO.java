package com.jgalante.balance.persistence;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;

import com.jgalante.balance.entity.Category;
import com.jgalante.balance.entity.Transaction;
import com.jgalante.crud.entity.BaseEntity;
import com.jgalante.crud.persistence.CrudDAO;

public class TransactionDAO extends CrudDAO {

	private static final long serialVersionUID = 1L;
	
	@Override
	public <T extends BaseEntity> List<T> search(int first, int pageSize,
			Map<String, Boolean> sort, Map<String, Object> filters) {
		addJoinFields("category");
		Map<String, Boolean> tmpSort = new LinkedHashMap<String, Boolean>();
		if (sort != null) {
			tmpSort.putAll(sort);			
		}
		tmpSort.put("transactionDate", true);
		return super.search(first, pageSize, tmpSort, filters);
	}
	
	public List<Category> findCategories() {
		CriteriaQuery<Category> cq = getEntityManager().getCriteriaBuilder().createQuery(Category.class);
		cq.select(cq.from(Category.class));
		return getEntityManager().createQuery(cq).getResultList();
	}

	public BigDecimal currentBalance() {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT SUM(t.value) FROM ");
		sb.append(Transaction.class.getName());
		sb.append(" AS t ");
		sb.append(" LEFT JOIN t.category c ");
		sb.append(" WHERE c.positive is :positive ");
		TypedQuery<BigDecimal> query = getEntityManager().createQuery(sb.toString(),BigDecimal.class);
		query.setParameter("positive", true);
		BigDecimal income = query.getSingleResult();
		query.setParameter("positive", false);
		BigDecimal outcome = query.getSingleResult();
		return income.subtract(outcome);
	}
	
	/*@Transactional
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
		sb.append(" LEFT JOIN FETCH o.person pe ");
		sb.append(" LEFT JOIN FETCH s.person pe1 ");
		sb.append(" WHERE p = ");
		sb.append(idParent);
		sb.append(" ORDER BY o.order, o.id, s.id, pe.name, pe1.name ASC");
		
		List<Category> categories = getDAO().findByJpql(sb.toString());
		return categories;
	}*/
	
	/*@Transactional
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
	}*/
	
	/*	
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
*/
}
