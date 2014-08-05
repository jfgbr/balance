package com.jgalante.balance.view;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.jgalante.balance.facade.IController;
import com.jgalante.balance.facade.IDAO;
import com.jgalante.balance.qualifier.Controller;
import com.jgalante.jgcrud.entity.BaseEntity;

public class BaseView<T extends BaseEntity, C extends IController<T, ? extends IDAO>>
		implements Serializable {

	private static final long serialVersionUID = 1L;

	private T entity;
	private List<T> entities;

	@Inject
	@Controller
	private IController<T, ? extends IDAO> controller;
	
	public IController<T, ? extends IDAO> getController() {
		return controller;
	}

	public T getEntity() {
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
