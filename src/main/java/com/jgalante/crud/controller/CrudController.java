package com.jgalante.crud.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.jgalante.balance.controller.BaseController;
import com.jgalante.crud.entity.BaseEntity;
import com.jgalante.crud.facade.ICrudController;
import com.jgalante.crud.persistence.CrudDAO;
import com.jgalante.crud.util.Filter;

public class CrudController<T extends BaseEntity, D extends CrudDAO> extends
		BaseController<T, D> implements ICrudController<T, D> {

	private static final long serialVersionUID = 1L;
	
	protected List<Filter> filters;
	
	public List<T> searchAll(Map<String, Boolean> sort) {
		return getDAO().searchAll(sort);
	}

	@Override
	public List<T> search(int first, int pageSize, Map<String, Boolean> sort,
			Map<String, Object> filters) {
		getDAO().cleanFilter();
		getDAO().addFilters(this.filters);		
		return ((CrudDAO) getDAO()).search(first, pageSize, sort, filters);
	}

	@Override
	public int rowCount() {
		return ((CrudDAO) getDAO()).rowCount();
	}

	public void addFilter(Filter filter) {
		if (filter != null) {
			if (filters == null) {
				filters = new LinkedList<Filter>();
			}
			filters.add(filter);
		}
//		getDAO().cleanFilter();
//		getDAO().addFilter(filter);
	}
	
	public void cleanFilter() {
		if (filters != null) {
//		getDAO().cleanFilter();
			filters.clear();
			filters = null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public D getDAO() {
		return (D)super.getDAO();
	}
}
