package com.jgalante.balance.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.jgalante.crud.entity.BaseEntity;

@Entity(name = "balance")
public class Balance extends BaseEntity {

	@JoinColumn(name = "id_account", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Account account;
	
	@JoinColumn(name = "id_transaction")
	@ManyToOne(fetch = FetchType.LAZY)
	private Transaction transaction;

	@Column(name = "dt_balance")
	private Date balanceDate;

	@Column(name = "is_positive", nullable = false)
	private Boolean positive = true;

	@Column(name = "vl_value", precision = 19, scale = 4)
	private BigDecimal value;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
	
	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public Date getBalanceDate() {
		return balanceDate;
	}

	public void setBalanceDate(Date balanceDate) {
		this.balanceDate = balanceDate;
	}

	public Boolean getPositive() {
		return positive;
	}

	public void setPositive(Boolean positive) {
		this.positive = positive;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	@Transient
	public BigDecimal getSignedValue() {
		return positive || BigDecimal.ZERO.equals(getValue()) ? getValue() : getValue().negate();
	}
}
