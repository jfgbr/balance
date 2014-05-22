package com.jgalante.balance.controller;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.jgalante.balance.persistence.DAO;
import com.jgalante.balance.persistence.IDAO;
import com.jgalante.balance.persistence.TesteDAO;
import com.jgalante.jgcrud.entity.BaseEntity;

public class TesteController<T extends BaseEntity, D extends TesteDAO> implements IController<T, D>, Serializable {
	
	private static final long serialVersionUID = 1L;

	@Inject
	@DAO
	private IDAO dao;
	
	private Class<T> entityClass;
	private Class<D> daoClass;

	@Override
	public Class<T> getEntityClass() {
		return entityClass;
	}
	
	@Override
	public void setEntityClass(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	public IDAO getDAO() {
		return dao;
	}

	@Override
	public Class<D> getDAOClass() {
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
	
}
