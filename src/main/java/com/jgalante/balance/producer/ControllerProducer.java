package com.jgalante.balance.producer;

import java.lang.reflect.Type;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;

import com.jgalante.balance.facade.IController;
import com.jgalante.balance.facade.IDAO;
import com.jgalante.balance.qualifier.Controller;
import com.jgalante.crud.entity.BaseEntity;
import com.jgalante.crud.util.ClassHelper;
import com.jgalante.crud.util.Util;

public class ControllerProducer {

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
				Type[] typeArguments = ClassHelper.getTypeArguments(((Class<?>) ip
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

}
