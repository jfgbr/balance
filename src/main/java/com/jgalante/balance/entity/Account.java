package com.jgalante.balance.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import com.jgalante.crud.entity.BaseEntity;


@Entity(name = "account")
public class Account extends BaseEntity {

	@Column(name = "ds_text", nullable = false)
	private String text;
	
	@JoinColumn(name = "id_type", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private AccountType type;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
	private Set<Transaction> transactions;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
	@OrderBy("dt_balance ASC")
	private Set<Balance> balances;

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the type
	 */
	public AccountType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(AccountType type) {
		this.type = type;
	}

	/**
	 * @return the transactions
	 */
	public Set<Transaction> getTransactions() {
		return transactions;
	}

	/**
	 * @param transactions the transactions to set
	 */
	public void setTransactions(Set<Transaction> transactions) {
		this.transactions = transactions;
	}

	/**
	 * @return the balances
	 */
	public Set<Balance> getBalances() {
		return balances;
	}

	/**
	 * @param balances the balances to set
	 */
	public void setBalances(Set<Balance> balances) {
		this.balances = balances;
	}

}
