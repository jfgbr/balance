package com.jgalante.balance.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.jgalante.balance.facade.IController;
import com.jgalante.balance.facade.IDAO;
import com.jgalante.balance.persistence.BaseDAO;
import com.jgalante.balance.qualifier.DAO;
import com.jgalante.balance.util.ClassHelper;
import com.jgalante.jgcrud.entity.BaseEntity;

public class BaseController<T extends BaseEntity, D extends BaseDAO> implements IController<T, D>, Serializable {
	
	private static final long serialVersionUID = 1L;

	@Inject
	@DAO
	private IDAO dao;
	
	private Class<T> entityClass;
	private Class<D> daoClass;

	private int rowTotal;

	@SuppressWarnings("unchecked")
	@Override
	public Class<T> getEntityClass() {
		if (entityClass == null) {
			entityClass = (Class<T>)ClassHelper.getClass(this.getClass(), 0);
		}
		return entityClass;
	}
	
	@Override
	public void setEntityClass(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	public IDAO getDAO() {
		return dao;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<D> getDAOClass() {
		if (daoClass == null) {
			daoClass = (Class<D>)ClassHelper.getClass(this.getClass(), 1);
		}
		return daoClass;
	}

	@Override
	public void setDAOClass(Class<D> daoClass) {
		this.daoClass = daoClass;
	}

	@Override
	public T find(Object id) {
		return getDAO().find(id);
	}

	@Override
	public T save(T entity) {
		return getDAO().save(entity);
	}

	@Override
	public List<T> findAll() {
		return getDAO().findAll(true);
	}

	@Override
	public T remove(T entity) {
		return getDAO().remove(entity);
	}

	@Override
	public List<T> search(int first, int pageSize, Map<String, Boolean> sort,
			Map<String, String> filters) {
		List<T> result = findAll();
		rowTotal = result.size();
		return result;
	}

	@Override
	public int rowCount() {
		// TODO Auto-generated method stub
		return rowTotal;
	}
	
	
}
