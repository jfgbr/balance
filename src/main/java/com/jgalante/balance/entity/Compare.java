package com.jgalante.balance.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.jgalante.crud.entity.BaseEntity;

public class Compare extends BaseEntity {

	private Account account;

	private BigDecimal balanceValue;

	private BigDecimal estimateValue;
	
	private BigDecimal differenceValue;

	private Date date;
	
	/**
	 * 
	 */
	public Compare() {
	}

	/**
	 * @param account
	 * @param balanceValue
	 * @param estimateValue
	 * @param date
	 */
	public Compare(Account account, BigDecimal balanceValue,
			BigDecimal estimateValue, Date date) {
		
		super();
		this.account = account;
		this.balanceValue = balanceValue;
		this.estimateValue = estimateValue;
		this.date = date;
		
	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * @param account
	 *            the account to set
	 */
	public void setAccount(Account account) {
		this.account = account;
	}

	/**
	 * @return the balanceValue
	 */
	public BigDecimal getBalanceValue() {
		return balanceValue;
	}

	/**
	 * @param balanceValue
	 *            the balanceValue to set
	 */
	public void setBalanceValue(BigDecimal balanceValue) {
		this.balanceValue = balanceValue;
	}

	/**
	 * @return the estimateValue
	 */
	public BigDecimal getEstimateValue() {
		return estimateValue;
	}

	/**
	 * @param estimateValue
	 *            the estimateValue to set
	 */
	public void setEstimateValue(BigDecimal estimateValue) {
		this.estimateValue = estimateValue;
	}

	/**
	 * @return the diferenceValue
	 */
	public BigDecimal getDifferenceValue() {
		if (differenceValue == null) {
			if (estimateValue != null) {
				differenceValue = balanceValue.subtract(estimateValue);
			} else {
				differenceValue = balanceValue;
			}
		}
		return differenceValue;
	}

	/**
	 * @param differenceValue the diferenceValue to set
	 */
	public void setDifferenceValue(BigDecimal differenceValue) {
		this.differenceValue = differenceValue;
	}
	
	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

}
