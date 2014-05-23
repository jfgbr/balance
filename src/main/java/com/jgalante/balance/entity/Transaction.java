package com.jgalante.balance.entity;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.jgalante.jgcrud.entity.BaseEntity;

@Entity
public class Transaction extends BaseEntity {

	@JoinColumn(name = "id_person")
	@ManyToOne(fetch = FetchType.LAZY)
	private Person person;

	@JoinColumn(name = "id_type", nullable = false)
	@ManyToOne(fetch = FetchType.EAGER)
	private TransactionType transactionType;

	@JoinColumn(name = "id_category", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Category category;

	@JoinColumn(name = "id_parent")
	@ManyToOne(fetch = FetchType.LAZY)
	private Transaction parent;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
	private Set<Transaction> subTransactions;

	@Column
	private Date transactionDate;

	@Column(name = "value", precision = 19, scale = 4)
	private BigDecimal value;

	@Column(name = "installments")
	private Integer installment = 1;

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Transaction getParent() {
		return parent;
	}

	public void setParent(Transaction parent) {
		this.parent = parent;
	}

	public Set<Transaction> getSubTransactions() {
		return subTransactions;
	}

	public void setSubTransactions(Set<Transaction> subTransactions) {
		this.subTransactions = subTransactions;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public Integer getInstallment() {
		return installment;
	}

	public void setInstallment(Integer installment) {
		this.installment = installment;
	}

}
