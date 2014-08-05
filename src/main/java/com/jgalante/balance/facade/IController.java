package com.jgalante.balance.facade;

import java.util.List;

import com.jgalante.jgcrud.entity.BaseEntity;

public interface IController<T extends BaseEntity, D extends IDAO> {

	public Class<T> getEntityClass();
	public void setEntityClass(Class<T> entityClass);
	
	public Class<D> getDAOClass();
	public void setDAOClass(Class<D> daoClass);
	
	public T find(Object id);
	public T save(T entity);
	public List<T> findAll();
}
