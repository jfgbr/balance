package com.jgalante.balance.persistence;

import java.util.List;

import com.jgalante.jgcrud.entity.BaseEntity;

public interface IDAO {
	public Class<? extends BaseEntity> getEntityClass();
	public void setEntityClass(Class<? extends BaseEntity> entityClass);
	
	public <T extends BaseEntity> T find(Object id);
	public <T extends BaseEntity> T save(T entity);
	public <T extends BaseEntity> List<T> findAll(boolean ascending, String... sort);
	public <T extends BaseEntity> List<T> findAll(Class<T> selectClass, boolean ascending, String... sort);
	
}
