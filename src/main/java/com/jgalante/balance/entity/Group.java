package com.jgalante.balance.entity;

public class Group {

	private String title;
	private Integer level;
	private Category category;
	private Transaction transaction;

	public Group() {
		super();
	}

	public Group(Category category, Transaction transaction) {
		super();
		this.category = category;
		if (transaction == null) {
			this.transaction = new Transaction();
		} else {
			this.transaction = transaction;
		}
		this.title = category.getText();
		this.level = category.getLevel();
	}

	public String getTitle() {

		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

}
