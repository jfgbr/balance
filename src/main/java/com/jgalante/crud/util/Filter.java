package com.jgalante.crud.util;

import java.util.List;

public class Filter {

	private String key;

	private Object value;

	private Operator op;

	private String alias;

	private Sort sort;

	private List<Filter> filters;

	public enum Operator {
		AND, OR, LIKE, EQUAL, NOT_EQUAL, BETWEEN, IN, NOT_IN, EQUAL_GREATER, EQUAL_LESS, IS_NULL, IS_NOT_NULL, IS_TRUE, IS_FALSE, JOIN, GROUPBY
	}
	
	public enum Sort {
		ASC, DESC, NONE
	}

	public Filter(String key, Object value) {
		this.key = key;
		this.value = value;
		this.op = Operator.EQUAL;
		this.sort = Sort.NONE;
		this.filters = null;
	}
	
	public Filter(String key, Operator op) {
		this.key = key;
		this.op = op;
	}

	public Filter(String key, Object value, Sort sort) {
		this.key = key;
		this.value = value;
		this.op = Operator.EQUAL;
		this.sort = sort;
		this.filters = null;
	}

	public Filter(String key, Object value, Operator op) {
		this.key = key;
		this.value = value;
		this.op = op;
		this.sort = Sort.NONE;
		this.filters = null;
	}

	public Filter(Operator op, List<Filter> filters) {
		this.key = null;
		this.value = null;
		this.op = op;
		this.sort = Sort.NONE;
		this.filters = filters;
	}

	public Filter(String key, Object value, Operator op, List<Filter> filters) {
		this.key = key;
		this.value = value;
		this.op = op;
		this.sort = Sort.NONE;
		this.filters = filters;
	}

	public Filter(String key, Object value, Operator op, Sort sort,
			List<Filter> filters) {
		this.key = key;
		this.value = value;
		this.op = op;
		this.sort = sort;
		this.filters = filters;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Operator getOp() {
		return op;
	}

	public void setOp(Operator op) {
		this.op = op;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Sort getSort() {
		return sort;
	}

	public void setSort(Sort sort) {
		this.sort = sort;
	}

	public List<Filter> getFilters() {
		return filters;
	}

	public void setFilters(List<Filter> filters) {
		this.filters = filters;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("key[");
		sb.append(key);
		sb.append("];value[");
		sb.append(value);
		sb.append("];operator[");
		sb.append((op != null)?op.name():null);
		sb.append("]");
		return key;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((filters == null) ? 0 : filters.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((op == null) ? 0 : op.hashCode());
		result = prime * result + ((sort == null) ? 0 : sort.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		Filter other = (Filter) obj;
		if (filters == null) {
			if (other.filters != null)
				return false;
		} else if (!filters.equals(other.filters))
			return false;
		
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		
		if (op != other.op)
			return false;
		
		if (sort != other.sort)
			return false;
		
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		
		return true;
	}
	
	
}
