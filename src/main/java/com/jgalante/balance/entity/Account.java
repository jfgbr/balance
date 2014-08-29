package com.jgalante.balance.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.jgalante.crud.entity.BaseEntity;


@Entity(name = "account")
public class Account extends BaseEntity {

	@Column(name = "ds_text", nullable = false)
	private String text;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
	private Set<Transaction> transactions;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Set<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(Set<Transaction> transactions) {
		this.transactions = transactions;
	}

}
