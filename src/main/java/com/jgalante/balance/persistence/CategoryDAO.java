package com.jgalante.balance.persistence;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.jgalante.balance.entity.Category;
import com.jgalante.balance.entity.Transaction;
import com.jgalante.crud.entity.BaseEntity;
import com.jgalante.crud.persistence.CrudDAO;
import com.jgalante.crud.util.Util;
import com.jgalante.crud.util.Filter;
import com.jgalante.crud.util.Filter.Operator;

public class CategoryDAO extends CrudDAO{

	private static final long serialVersionUID = 1L;
	
	@Override
	public <T extends BaseEntity> List<T> searchAll(Map<String, Boolean> sort) {
		getQueryParam().reset();
		addJoinFields("person");
		addJoinFields("parent");
		Map<String, Boolean> tmpSort = new LinkedHashMap<String, Boolean>();
		if (sort != null) {
			tmpSort.putAll(sort);
		}
		tmpSort.put("id", false);
		return super.searchAll(tmpSort);
	}

	
	@Transactional
	public List<Category> findCategoryByParent(Long idParent) {
//		(Long id, String text, Long idParent, String textParent, Person person,
//				Boolean positive, Integer order)
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT DISTINCT NEW ");
		sb.append(Category.class.getName());
		sb.append(" (c.id, c.text, s.id, s.text, p, c.positive, c.order) FROM "); 
		sb.append(Category.class.getName());
		sb.append(" AS c ");
		sb.append(" LEFT JOIN c.person p ");
		sb.append(" LEFT JOIN c.parent s ");
		sb.append(" WHERE c.parent.id = ");
		sb.append(idParent);
		sb.append(" ORDER BY c.order, c.text ASC");		
		List<Category> categories = findByJpql(sb.toString());
		return categories;
	}
	

	@Transactional
	public List<Category> findGroupsByParent(Long idParent, Long idAccount, Calendar startDate, Calendar endDate) {
		
//		StringBuffer sb = new StringBuffer();
//		sb.append("SELECT DISTINCT object(c) FROM ");
//		sb.append(Category.class.getName());
//		sb.append(" AS c ");
//		sb.append(" LEFT JOIN FETCH c.subCategories s ");
//		sb.append(" LEFT JOIN FETCH s.transactions t ");
//		sb.append(" WHERE c.parent.id is null ");
//		if (idParent != null) {
//			sb.append(" AND s.parent.id = ");
//			sb.append(idParent);
//		}
//		Filter filter = null;
//		QueryParam queryParam = new QueryParam("t", Transaction.class);
//		if(startDate != null) {
//			filter = queryParam.defineFilter(Date.class, "transactionDate", startDate);
//			sb.append(" AND ");
//			sb.append(queryParam.createWhere(filter));
//		}
//		sb.append(" GROUP BY YEAR(t.transactionDate), MONTH(t.transactionDate), c.id, s.id");
//		sb.append(" ORDER BY c.order, c.text, s.order, s.text, YEAR(t.transactionDate), MONTH(t.transactionDate)");
//		TypedQuery<Category> query = (TypedQuery<Category>) getEntityManager().createQuery(sb.toString(),
//				Category.class);		
//		if (filter != null) {
//			queryParam.updateParameter(query, filter);
//		}
		getQueryParam().reset();
		List<Filter> filters = new LinkedList<Filter>();
		Filter rootFilter = new Filter(Operator.AND, filters);
		filters.add(new Filter("parent.id", Operator.IS_NULL));
		if (idParent != null) {
			filters.add(new Filter("subCategories.parent.id", idParent));
		}
		
		if (idAccount != null) {
			filters.add(new Filter("subCategories.transactions.account.id", idAccount));
		}
		
		if(startDate != null) {
			if (endDate != null) {
				String dates = String.format("%s; %s", Util.convertDateToString(startDate.getTime()), Util.convertDateToString(endDate.getTime()));
				filters.add(getQueryParam().defineFilter(Date.class, "subCategories.transactions.transactionDate", dates));
			} else {
				filters.add(new Filter("subCategories.transactions.transactionDate",startDate.getTime(),Operator.EQUAL_GREATER));
			}
		}
		filters = new LinkedList<Filter>();
		filters.add(rootFilter);
		addJoinFields("subCategories,subCategories.transactions");
//		getQueryParam().addGroupByFilter("subCategories.transactions.transactionDate,id,subCategories.id");
		
		getQueryParam().setIncludeJoins(true);
		Map<String, Boolean> sort = new LinkedHashMap<String, Boolean>();
		sort.put("order", false);
		sort.put("text", false);
		sort.put("subCategories.order", false);
		sort.put("subCategories.text", false);
		sort.put("subCategories.transactions.transactionDate", false);
		getQueryParam().setDistinct(true);
		TypedQuery<Category> query = createQuery(sort, filters);
		
		List<Category> categories = query.getResultList();
		return categories;
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return Category.class;
	}


	public Category findParentByCategory(Long id) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT DISTINCT s FROM "); 
		sb.append(Category.class.getName());
		sb.append(" AS c ");
		sb.append(" LEFT JOIN c.parent s ");
		sb.append(" WHERE c.id = ");
		sb.append(id);		
		Category category = singleResultByJpql(sb.toString());
		return category;
	}


	public BigDecimal currentBalance(Category category) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT SUM(CASE c.positive WHEN 1 THEN t.value ELSE (t.value * -1) END) FROM ");
		sb.append(Transaction.class.getName());
		sb.append(" AS t ");
		sb.append(" LEFT JOIN t.category c ");
		sb.append(" WHERE ");
		sb.append(" c.id = :categoryId ");
		sb.append(" OR c.parent.id = :categoryId ");
		TypedQuery<BigDecimal> query = getEntityManager().createQuery(sb.toString(),BigDecimal.class);
		query.setParameter("categoryId", category.getId());
		return query.getSingleResult();		
	}


	public List<Category> findTransferCategories() {
		getQueryParam().reset();
		List<Filter> filters = new LinkedList<Filter>();
		Filter rootFilter = new Filter(Operator.AND, filters);
		filters.add(new Filter("parent.id", Operator.IS_NULL));
		filters.add(new Filter("transfer", Operator.IS_TRUE));
		filters = new LinkedList<Filter>();
		filters.add(rootFilter);
		Map<String, Boolean> sort = new LinkedHashMap<String, Boolean>();
		sort.put("order", false);
		sort.put("text", false);
		TypedQuery<Category> query = createQuery(sort, filters);
		
		List<Category> categories = query.getResultList();
		return categories;
	}


	public Category findTransferFromParent(Long id, Boolean positive) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT DISTINCT c FROM "); 
		sb.append(Category.class.getName());
		sb.append(" AS c ");
		sb.append(" WHERE c.parent.id = ");
		sb.append(id);		
		sb.append(" AND c.positive = ");
		sb.append(positive.toString());	
		sb.append(" AND c.transfer is TRUE ");
		Category category = singleResultByJpql(sb.toString());
		return category;
	}

}
