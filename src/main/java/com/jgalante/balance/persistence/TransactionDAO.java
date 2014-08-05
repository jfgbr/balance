package com.jgalante.balance.persistence;

import java.util.List;

import javax.persistence.criteria.CriteriaQuery;

import com.jgalante.balance.entity.Category;

public class TransactionDAO extends BaseDAO {

	private static final long serialVersionUID = 1L;
	
	public List<Category> findCategories() {
		CriteriaQuery<Category> cq = getEntityManager().getCriteriaBuilder().createQuery(Category.class);
		cq.select(cq.from(Category.class));
		return getEntityManager().createQuery(cq).getResultList();
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
