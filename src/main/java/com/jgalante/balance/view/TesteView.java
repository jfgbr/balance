package com.jgalante.balance.view;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.jgalante.balance.controller.IController;
import com.jgalante.balance.persistence.IDAO;
import com.jgalante.jgcrud.entity.BaseEntity;

public class TesteView<T extends BaseEntity, C extends IController<T, ? extends IDAO>>
		implements Serializable {

	private static final long serialVersionUID = 1L;

	private T entity;
	private List<T> entities;

	@Inject
	@Controller
	private IController<T, ? extends IDAO> controller;
	
//	private C control;
	
//	public C getControl() {
//		return (C)controller;
//	}
//
//	public void setControl(C control) {
//		this.control = control;
//	}

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
