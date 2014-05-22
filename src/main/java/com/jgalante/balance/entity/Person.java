package com.jgalante.balance.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.jgalante.jgcrud.entity.BaseEntity;

@Entity
public class Person extends BaseEntity {

	@Column(name="name", nullable = false)
	private String name;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "person")
	private Set<Transaction> transactions;
	
	public Person() {
		super();
	}

	public Person(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(Set<Transaction> transactions) {
		this.transactions = transactions;
	}

}
