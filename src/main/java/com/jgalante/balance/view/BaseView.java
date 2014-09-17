package com.jgalante.balance.view;

import java.util.List;

import javax.inject.Inject;

import com.jgalante.balance.facade.IController;
import com.jgalante.balance.facade.IDAO;
import com.jgalante.balance.qualifier.Controller;
import com.jgalante.crud.entity.BaseEntity;
import com.jgalante.crud.util.Util;
import com.jgalante.crud.view.SimpleView;

public class BaseView<T extends BaseEntity, C extends IController<T, ? extends IDAO>>
		extends SimpleView {

	private static final long serialVersionUID = 1L;

	private Class<T> entityClass;
	private T entity;
	private List<T> entities;
	

	@Inject
	@Controller
	private IController<T, ? extends IDAO> controller;
	
	protected T createEntity(){
		try {
			return (T) getEntityClass().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Class<T> getEntityClass() {
		if (entityClass == null) {
			entityClass = (Class<T>)Util.getClass(this.getClass(), 0);
		}
		return entityClass;
	}
	
	public IController<T, ? extends IDAO> getController() {
		return controller;
	}

	public T getEntity() {
		if (entity == null) {
			entity = createEntity();
		}
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}

	public List<T> getEntities() {
		return entities;
	}

	public void setEntities(List<T> entities) {
		this.entities = entities;
	}

}
