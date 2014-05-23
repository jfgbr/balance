package com.jgalante.balance.persistence;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import com.jgalante.jgcrud.annotation.DataRepository;
import com.jgalante.jgcrud.entity.BaseEntity;

public class TesteDAO implements IDAO, Serializable {

	private static final long serialVersionUID = 1L;

	private Class<? extends BaseEntity> entityClass;

	@Inject
	@DataRepository
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public <T extends BaseEntity> T find(Object id) {
		return (T) this.entityManager.find(getEntityClass(), id);
	}

	@Override
	public <T extends BaseEntity> List<T> findByJpql(String jpql) {
		List<T> results = null;
		Query query = getEntityManager().createQuery(jpql);
		results = query.getResultList();
		return results;
	}
	
	@Transactional
	public <T extends BaseEntity> T save(T entity) {
		return getEntityManager().merge(entity);
	}

	// public List<? extends BaseEntity> findAll() {
	// CriteriaQuery<? extends BaseEntity> cq =
	// getEntityManager().getCriteriaBuilder().createQuery(getEntityClass());
	// cq.multiselect(cq.from(getEntityClass()));
	// return getEntityManager().createQuery(cq).getResultList();
	// }

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
	
	private String defineSortFields(String alias, boolean ascending, String... sort) {
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

	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return entityClass;
	}

	@Override
	public void setEntityClass(Class<? extends BaseEntity> entityClass) {
		this.entityClass = entityClass;
	}

}
