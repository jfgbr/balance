package com.jgalante.balance.view;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.jgalante.balance.controller.TemController;
import com.jgalante.jgcrud.entity.BaseEntity;

@Named
@ViewScoped
public class TemView  extends TesteView<BaseEntity, TemController> {

	private static final long serialVersionUID = 1L;

	@PostConstruct
	public void init() {
		System.out.println(getEntityClass());
	}

	public String getEntityClass() {
		return getController().getEntityClass().toString();
	}
}
