package com.jgalante.balance.entity;

import java.util.Set;

public class Group {

	private Category category;
	private Set<Transaction> transactions;

	public Group() {
		super();
	}
	
	public Group(Category category, Set<Transaction> transactions) {
		super();
		this.category = category;
		this.transactions = transactions;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Set<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(Set<Transaction> transactions) {
		this.transactions = transactions;
	}

}
