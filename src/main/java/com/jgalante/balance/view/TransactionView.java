package com.jgalante.balance.view;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.jgalante.balance.controller.TransactionController;
import com.jgalante.balance.entity.Transaction;

@Named
@ViewScoped
public class TransactionView extends CrudView<Transaction, TransactionController> {

	private static final long serialVersionUID = 1L;

	
}
