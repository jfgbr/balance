package com.jgalante.balance.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import com.jgalante.balance.facade.IController;
import com.jgalante.balance.facade.IDAO;
import com.jgalante.jgcrud.entity.BaseEntity;

public class DelegateDataModel<T extends BaseEntity, C extends IController<T, ? extends IDAO>> extends LazyDataModel<T> {

	private static final long serialVersionUID = 1L;

	protected List<T> datasource;

	protected int currentPage;

	private boolean primeira = true;

	private Field fieldId;
	
	protected IController<T, ? extends IDAO> delegate;

	private String fetchDataSource = "search";
	
	public DelegateDataModel(IController<T, ? extends IDAO> controller) {
		this.delegate = controller;
	}

	@SuppressWarnings("unchecked")
	public List<T> fetchDataSource(int first, int pageSize, Map<String, Boolean> sort, Map<String, String> filters) {
		try {
			Method search = delegate.getClass().getMethod(fetchDataSource, int.class, int.class, Map.class, Map.class);
			return (List<T>) search.invoke(delegate, first, pageSize, sort, filters);
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}

		return delegate.search(first, pageSize, sort, filters);
	}

	public String getFetchDataSource() {
		return this.fetchDataSource;
	}

	public void setFetchDataSource(String fetchDataSource) {
		this.fetchDataSource = fetchDataSource;
	}

	public int getTotalSize() {
		return delegate.rowCount();
	}

	@Override
	public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {

		Map<String, Boolean> sort = null;
		if (sortField != null) {
			sort = new HashMap<String, Boolean>();
			sort.put(sortField, SortOrder.DESCENDING.equals(sortOrder));
		}
		return load(first, pageSize, sort, filters);
	}

	public void setDatasource(List<T> datasource) {
		this.datasource = datasource;
	}

	@Override
	public List<T> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, String> filters) {
		Map<String, Boolean> sort = null;
		if (multiSortMeta != null) {
			sort = new HashMap<String, Boolean>();
			for (SortMeta sortMeta : multiSortMeta) {
				sort.put(sortMeta.getSortField(), SortOrder.DESCENDING.equals(sortMeta.getSortOrder()));
			}
		}
		return load(first, pageSize, sort, filters);
	}

	@SuppressWarnings("unchecked")
	private List<T> load(int first, int pageSize, Map<String, Boolean> sort, Map<String, String> filters) {

		if (datasource == null) {
			datasource = new ArrayList<T>();
		}

		List<T> wrappedData = (List<T>) getWrappedData();

		if (primeira && wrappedData != null && !wrappedData.isEmpty()) {
			datasource = (List<T>) getWrappedData();
		} else {
			datasource = fetchDataSource(first, pageSize, sort, filters);
		}
		primeira = false;
		this.setRowCount(getTotalSize());

		currentPage = calculateCurrentPage(first, pageSize);

		setWrappedData(datasource);

		return datasource;
	}

	protected int calculateCurrentPage(int first, int pageSize) {
		return ((first / pageSize) + 1);
	}
	
	@Override
	public T getRowData(String rowKey) {
		for (T bean : datasource) {
			try {
				if (getId(bean).equals(ClassHelper.convertIfNeeded(rowKey, getFieldId(bean).getType()))) {
					return bean;
				}
			} catch (IllegalArgumentException e) {
			} catch (ClassCastException e) {
			} catch (UnsupportedOperationException e) {
			} catch (IllegalAccessException e) {
			}
		}

		return super.getRowData(rowKey);
	}

	@Override
	public Object getRowKey(T object) {
		try {
			return getId(object);
		} catch (IllegalArgumentException e) {
		} catch (UnsupportedOperationException e) {
		} catch (IllegalAccessException e) {
		}
		return super.getRowKey(object);
	}

	private Field getFieldId(T object) throws UnsupportedOperationException {
		if (fieldId == null) {
			fieldId = ClassHelper.getField(object.getClass(), "id");
			if (fieldId == null) {
				throw new UnsupportedOperationException(
						"getRowData(String rowKey) and getRowKey(T object) must be implemented when basic rowKey algorithm is not used.");
			}
		}
		return fieldId;
	}

	private Object getId(T object) throws IllegalArgumentException, IllegalAccessException, UnsupportedOperationException {
		return getFieldId(object).get(object);
	}
}
