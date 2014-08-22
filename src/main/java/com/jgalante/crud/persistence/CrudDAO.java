package com.jgalante.crud.persistence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.jgalante.balance.persistence.BaseDAO;
import com.jgalante.crud.entity.BaseEntity;
import com.jgalante.crud.util.Filter;
import com.jgalante.crud.util.Filter.Operator;
import com.jgalante.crud.util.QueryParam;

public class CrudDAO extends BaseDAO {

	private static final long serialVersionUID = 1L;
	
	private QueryParam queryParam;

	private List<String> joinFields;

	@Transactional
	public <T extends BaseEntity> List<T> search(int first, int pageSize,
			Map<String, Boolean> sort, Map<String, Object> filters) {

		TypedQuery<T> querySearch = createQuery(sort, filters);
		return searchQuery(first, pageSize, querySearch);
	}
	
	@Transactional
	public <T extends BaseEntity> List<T> search(int first, int pageSize,
			Map<String, Boolean> sort, List<Filter> filters) {

		TypedQuery<T> querySearch = createQuery(sort, filters);
		return searchQuery(first, pageSize, querySearch);
	}

	protected <T extends BaseEntity> List<T> searchQuery(int first,
			int pageSize, TypedQuery<T> querySearch) {
		Query queryCount = null;
		List<T> data = new ArrayList<T>();
		try {
			if (pageSize != 0) {
				querySearch.setFirstResult(first).setMaxResults(pageSize);
			}

			data = querySearch.getResultList();

			queryCount = createCountQuery(queryParam.createCountJPQL());
			queryParam.setRowCount(
					((Number) queryCount.getSingleResult()).intValue());

		} catch (Exception e) {
			System.out.println(querySearch.toString());
			System.out.println(queryCount.toString());
			e.printStackTrace();
		}

		return data;
	}

	public int rowCount() {
		return queryParam.getRowCount();
	}
	
	private Query createCountQuery(String countQuery) {
		Query queryCount = getEntityManager().createQuery(countQuery);
		queryParam.updateParameter(queryCount);
		return queryCount;
	}

	
	protected <T extends BaseEntity> TypedQuery<T> createQuery(
			Map<String, Boolean> sort, Map<String, Object> filters) {
		// Use getQueryParam() to create queryParam in case is null
		List<Filter> listFilters = getQueryParam().createListFiltersFromMap(filters);
//		listFilters.addAll(createListFilterForJoinFields());
//		getQueryParam().addJoinFilters(joinFields);		

		return createQuery(sort, listFilters);
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends BaseEntity> TypedQuery<T> createQuery(
			Map<String, Boolean> sort, List<Filter> filters) {
		
		// Use getQueryParam() to create queryParam in case is null		
		getQueryParam().setOrderBy(sort);

		addFilters(filters);

		String jpql = queryParam.createSearchJPQL();
		TypedQuery<T> query = (TypedQuery<T>) getEntityManager().createQuery(jpql,
				getEntityClass());
		queryParam.updateParameter(query);
		return query;
	}

	protected List<Filter> createListFilterForJoinFields() {
		List<Filter> listFilters = new LinkedList<Filter>();
		if (joinFields != null) {
			for (String joinField : joinFields) {
				listFilters.add(new Filter(joinField, null, Operator.JOIN));
			}
		}
		return listFilters;
	}
	
	protected void addJoinFields(String joinFields) {
		if (joinFields != null) {
			this.joinFields = new LinkedList<String>(Arrays.asList(joinFields
					.split(",")));
			getQueryParam().addJoinFilters(this.joinFields);
		}
	}

	public void addFilters(List<Filter> filters) {
		getQueryParam().addFilters(filters);
	}
	
	public void addFilter(Filter filter) {
		getQueryParam().addFilter(filter);
	}
	
	public void cleanFilter() {
		getQueryParam().setFilters(null);		
	}
	
 	public QueryParam getQueryParam() {
		if (queryParam == null) {
			queryParam = new QueryParam(getEntityClass());
		}
		return queryParam;
	}
	
}
