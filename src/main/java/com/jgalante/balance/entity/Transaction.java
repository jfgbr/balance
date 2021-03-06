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


@Entity(name="transaction")
public class Transaction extends BaseEntity {

	@JoinColumn(name = "id_category", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Category category;
	
	@JoinColumn(name = "id_account", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Account account;
	
	@Column(name = "ds_text", nullable = false)
	private String text;

	@Column(name = "dt_transaction")
	private Date transactionDate;

	@Column(name = "vl_value", precision = 19, scale = 4)
	private BigDecimal value;

	
	@Transient
	public void addValue(BigDecimal newValue) {
		if (newValue != null) {
			BigDecimal value;
			if (this.value != null) {
				value = getValue().add(newValue);
			} else {
				value = newValue;
			}
			/*if (value.compareTo(BigDecimal.ZERO) == -1) {
				setTransactionType(TransactionType.NEGATIVE);			
			} else {
				setTransactionType(TransactionType.POSITIVE);
			}*/
			setValue(value.abs());
		}
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	@Transient
	public BigDecimal getValueAbsolute() {
		return value;
	}
	
	public BigDecimal getValue() {
		/*if (value != null) {
			return value.multiply(new BigDecimal(transactionType.getPositive()?1 : -1));
		} else {*/
			return value;
		//}
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
