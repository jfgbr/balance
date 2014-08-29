package com.jgalante.balance.facade;

import java.util.List;

import javax.persistence.EntityManager;

import com.jgalante.crud.entity.BaseEntity;


public interface IDAO {
	
	public EntityManager getEntityManager();
	
	public Class<? extends BaseEntity> getEntityClass();
	public void setEntityClass(Class<? extends BaseEntity> entityClass);
	
	public <T extends BaseEntity> T find(Object id);
	public <T extends BaseEntity> List<T> findByJpql(String jpql);
	public <T extends BaseEntity> T singleResultByJpql(String jpql);
	
	public <T extends BaseEntity> T save(T entity);
	public <T extends BaseEntity> T remove(T entity);
	public <T extends BaseEntity> List<T> findAll(boolean ascending, String... sort);
	public <T extends BaseEntity> List<T> findAll(Class<T> selectClass, boolean ascending, String... sort);
	
	
}
