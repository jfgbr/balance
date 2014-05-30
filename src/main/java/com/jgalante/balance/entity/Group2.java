package com.jgalante.balance.entity;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

public class Group2 {

	private String title;
	private Category category;
	private Transaction transaction;
	private boolean onlyCategory = false;
	private List<BigDecimal> values;

	public Group2() {
		super();
	}

	public Group2(Category category, Transaction transaction) {
		super();
		this.category = category;
		String name = "";
		if (transaction == null) {
			this.transaction = new Transaction();
		} else {
			this.transaction = transaction;
			addValue(this.getTransaction().getTransactionDate(),
					this.transaction.getValue());
			if (this.transaction.getPerson() != null) {
				name = " - " + this.transaction.getPerson().getName();
			}
		}
		this.title = category.getText() + name;
		if (this.transaction.getId() == null) {
			this.onlyCategory = true;
		}
	}

	public void addValue(Date date, BigDecimal value) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		Integer month = cal.get(Calendar.MONTH);

		if (values == null) {
			values = new LinkedList<BigDecimal>();
			for (int i = 0; i < 12; i++) {
				values.add(null);
			}
		}
		BigDecimal monthValue = values.get(month);
		if (monthValue == null) {
			values.set(month, value);
		} else {
			values.set(month, monthValue.add(value));
		}
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isOnlyCategory() {
		return onlyCategory;
	}

	public List<BigDecimal> getValues() {
		return values;
	}

	public void setValues(List<BigDecimal> values) {
		this.values = values;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((category == null) ? 0 : category.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Group2 other = (Group2) obj;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category)) {
			return false;
		} else if (transaction != null
				&& !transaction.equals(other.getTransaction())
				&& transaction.getPerson() != null
				&& !transaction.getPerson().equals(
						other.getTransaction().getPerson())) {
			return false;
		}
		
		return true;
	}

	
}
