package com.jgalante.crud.controller;

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

	@Override
	public List<T> search(int first, int pageSize, Map<String, Boolean> sort,
			Map<String, Object> filters) {
		return ((CrudDAO) getDAO()).search(first, pageSize, sort, filters);
	}

	@Override
	public int rowCount() {
		return ((CrudDAO) getDAO()).rowCount();
	}

	public void addFilter(Filter filter) {
		getDAO().cleanFilter();
		getDAO().addFilter(filter);
	}
	
	public void cleanFilter() {
		getDAO().cleanFilter();
	}
	
	@SuppressWarnings("unchecked")
	public D getDAO() {
		return (D)super.getDAO();
	}
}
