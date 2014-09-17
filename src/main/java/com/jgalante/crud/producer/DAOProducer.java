package com.jgalante.crud.producer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;

import com.jgalante.balance.facade.IDAO;
import com.jgalante.balance.persistence.BaseDAO;
import com.jgalante.balance.qualifier.DAO;
import com.jgalante.crud.entity.BaseEntity;
import com.jgalante.crud.util.Util;

public class DAOProducer {
	
	@SuppressWarnings("unchecked")
	@Produces
	@Dependent
	@DAO
	public <T extends BaseEntity> IDAO create(InjectionPoint ip, BeanManager bm) {

		if (ip.getAnnotated().isAnnotationPresent(DAO.class)) {
			DAO dao = ip.getAnnotated().getAnnotation(DAO.class);
			Class<T> entityClass = null;
			Class<? extends IDAO> daoClass = BaseDAO.class;
			
			if (dao.entityClass() != null && dao.entityClass().length > 0) {
				entityClass = dao.entityClass()[0];
			}
			
			try {
				Type[] typeArguments = ((ParameterizedType) ((Class<?>) ip
						.getBean().getBeanClass()).getGenericSuperclass())
						.getActualTypeArguments();
				entityClass = (Class<T>) typeArguments[0];
				daoClass = (Class<? extends IDAO>) typeArguments[1];
			} catch (Exception e) {}
			
			try {
				IDAO genericDAO = (IDAO) Util.getBeanByType(daoClass, bm);
				genericDAO.setEntityClass(entityClass);
				return genericDAO;
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}
		throw new IllegalArgumentException(
				"Annotation @DAO is required when injecting IDAO or subclasses");
	}
}
