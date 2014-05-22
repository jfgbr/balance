package com.jgalante.balance.view;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;

import com.jgalante.balance.controller.IController;
import com.jgalante.balance.persistence.DAO;
import com.jgalante.balance.persistence.IDAO;
import com.jgalante.jgcrud.entity.BaseEntity;
import com.jgalante.jgcrud.util.Util;

public class Producer {

	@SuppressWarnings("unchecked")
	@Dependent
	@Produces
	@Controller
	public <T extends BaseEntity> IController<T, IDAO> produceController(
			InjectionPoint ip, BeanManager bm) {

		if (ip.getAnnotated().isAnnotationPresent(Controller.class)) {
			Class<T> entityClass;
			Class<? extends IController<T, ? extends IDAO>> controllerClass = null;
			try {
				Type[] typeArguments = getTypeArguments(((Class<?>) ip
						.getBean().getBeanClass()));

				entityClass = (Class<T>) typeArguments[0];
				controllerClass = (Class<? extends IController<T, ? extends IDAO>>) typeArguments[1];
			} catch (Exception e) {
				entityClass = (Class<T>) BaseEntity.class;
				Class<?> tmpClass = IController.class;
				controllerClass = (Class<? extends IController<T, ? extends IDAO>>) tmpClass;
			}

			try {
				IController<T, ? extends IDAO> genericController = (IController<T, ? extends IDAO>) Util
						.getBeanByType(controllerClass, bm);
				genericController.setEntityClass(entityClass);
				return (IController<T, IDAO>)genericController;
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}
		throw new IllegalArgumentException(
				"Annotation @Controller is required when injecting GenericController or subclasses");
	}

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
        throw new IllegalArgumentException("Annotation @DAO is required when injecting GenericDAO or subclasses");
    }
	
	private Type getSuperClassType(Type type) {
		return ((Class<?>) type).getGenericSuperclass();
	}

	private Type[] getTypeArguments(Type type) {
		Type superClassType = getSuperClassType(type);
		Type[] typeArguments = null;
		try {
			typeArguments = ((ParameterizedType) (superClassType))
					.getActualTypeArguments();

		} catch (Exception e) {
			typeArguments = getTypeArguments(superClassType);
		}
		return typeArguments;
	}

}
