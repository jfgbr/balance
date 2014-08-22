package com.jgalante.balance.persistence;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.jgalante.balance.entity.Category;
import com.jgalante.crud.entity.BaseEntity;
import com.jgalante.crud.persistence.CrudDAO;
import com.jgalante.crud.util.ClassHelper;
import com.jgalante.crud.util.Filter;
import com.jgalante.crud.util.Filter.Operator;

public class CategoryDAO extends CrudDAO{

	private static final long serialVersionUID = 1L;

	
	public List<Category> findCategoryByParent(Long idParent) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT DISTINCT object(o) FROM ");
		sb.append(Category.class.getName());
		sb.append(" AS o ");
		sb.append(" LEFT JOIN FETCH o.parent p ");
		sb.append(" WHERE p.id = ");
		sb.append(idParent);
		sb.append(" ORDER BY o.order, o.text ASC");
		
		List<Category> categories = findByJpql(sb.toString());
		return categories;
	}
	

	@Transactional
	public List<Category> findGroupsByParent(Long idParent, Calendar startDate, Calendar endDate) {
		
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
		
		if(startDate != null) {
			if (endDate != null) {
				String dates = String.format("%s; %s", ClassHelper.convertDateToString(startDate.getTime()), ClassHelper.convertDateToString(endDate.getTime()));
				filters.add(getQueryParam().defineFilter(Date.class, "subCategories.transactions.transactionDate", dates));
			} else {
				filters.add(new Filter("subCategories.transactions.transactionDate",startDate.getTime(),Operator.EQUAL_GREATER));
			}
		}
		filters = new LinkedList<Filter>();
		filters.add(rootFilter);
		addJoinFields("subCategories,subCategories.transactions");
		getQueryParam().addGroupByFilter("subCategories.transactions.transactionDate,id,subCategories.id");
		
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
}
