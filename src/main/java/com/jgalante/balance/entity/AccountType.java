package com.jgalante.balance.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.jgalante.crud.entity.BaseEntity;


@Entity(name = "account_type")
public class AccountType extends BaseEntity {

	@Column(name = "ds_text", nullable = false)
	private String text;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "type")
	private Set<Account> accounts;
	
	public static AccountType CREDIT_CARD = new AccountType(3L, "Credit Card");
	
	/**
	 */
	public AccountType() {
		super();
	}
	
	/**
	 * @param text
	 * @param accounts
	 */
	public AccountType(Long id, String text) {
		super();
		setId(id);
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Set<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(Set<Account> accounts) {
		this.accounts = accounts;
	}

}
