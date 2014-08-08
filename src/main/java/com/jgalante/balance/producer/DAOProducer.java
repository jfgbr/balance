package com.jgalante.balance.producer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;

import com.jgalante.balance.facade.IDAO;
import com.jgalante.balance.qualifier.DAO;
import com.jgalante.jgcrud.entity.BaseEntity;
import com.jgalante.jgcrud.util.Util;

public class DAOProducer {
	
	@SuppressWarnings("unchecked")
	@Produces
	@Dependent
	@DAO
	public <T extends BaseEntity> IDAO create(InjectionPoint ip, BeanManager bm) {

		if (ip.getAnnotated().isAnnotationPresent(DAO.class)) {
			Type[] typeArguments = ((ParameterizedType) ((Class<?>) ip
					.getBean().getBeanClass()).getGenericSuperclass())
					.getActualTypeArguments();
			Class<T> entityClass = (Class<T>) typeArguments[0];
			Class<? extends IDAO> daoClass = (Class<? extends IDAO>) typeArguments[1];
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
