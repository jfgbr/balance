package com.jgalante.balance.persistence;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import com.jgalante.balance.facade.IDAO;
import com.jgalante.crud.annotation.DataRepository;
import com.jgalante.crud.entity.BaseEntity;

public class BaseDAO implements IDAO, Serializable {

	private static final long serialVersionUID = 1L;

	private Class<? extends BaseEntity> entityClass;

	@Inject
	@DataRepository
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public <T extends BaseEntity> T find(Object id) {
		return (T) this.entityManager.find(getEntityClass(), id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends BaseEntity> List<T> findByJpql(String jpql) {
		List<T> results = null;
		Query query = getEntityManager().createQuery(jpql);
		results = query.getResultList();
		return results;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends BaseEntity> T singleResultByJpql(String jpql) {
		T result = null;
		Query query = getEntityManager().createQuery(jpql);
		try {
		result = (T)query.getSingleResult();
		} catch (Exception e) {
			result = null;
		}
		return result;
	}
	
	@Transactional
	public <T extends BaseEntity> T save(T entity) {
		return getEntityManager().merge(entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public <T extends BaseEntity> T remove(T entity) {
		T removeEntity = (T) (getEntityManager().contains(entity) ? entity : getEntityManager().getReference(getEntityClass(), entity.getId()));
		getEntityManager().remove(removeEntity);
		return entity;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends BaseEntity> List<T> findAll(boolean ascending, String... sort) {
		return (List<T>) findAll(getEntityClass(), ascending, sort);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends BaseEntity> List<T> findAll(Class<T> selectClass, boolean ascending, String... sort) {
		List<T> results = null;
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT object(o) FROM ");
		sb.append(selectClass.getName());
		sb.append(" AS o");
		sb.append(defineSortFields("o", ascending, sort));
		Query query = getEntityManager().createQuery(sb.toString());
		results = query.getResultList();

		return results;
	}
	
	protected String defineSortFields(String alias, boolean ascending, String... sort) {
		StringBuffer sb = new StringBuffer();
		boolean first = true;
		for (String item : sort) {
			if (first) {
				sb.append(" ORDER BY ");
				first = false;
			} else {
				sb.append(", ");
			}
			sb.append(alias).append(".").append(item);
		}
		if (!first){
			sb.append((ascending?" ASC": " DESC"));
		}
		
		return sb.toString();
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

//	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
//		if (entityClass == null) {
//			entityClass = (Class<? extends BaseEntity>)ClassHelper.getClass(this.getClass(), 0);
//		}
		return entityClass;
	}

	@Override
	public void setEntityClass(Class<? extends BaseEntity> entityClass) {
		this.entityClass = entityClass;
	}


}
