package com.jgalante.balance.entity;

import java.util.List;

public class Group2 {

	private String title;
	private Integer level;
	private Category category;
	private Transaction transaction;
	private List<Group2> groups;

	public Group2() {
		super();
	}

	public Group2(Category category, Transaction transaction) {
		super();
		this.category = category;
		init(category, transaction);
		this.level = category.getLevel();
	}

	public Group2(Category category, Transaction transaction,
			List<Group2> groups) {
		super();
		this.category = category;
		init(category, transaction);
		this.level = category.getLevel();
		this.groups = groups;
	}

	private void init(Category category, Transaction transaction) {
		String name = "";
		if (transaction == null) {
			this.transaction = new Transaction();
		} else {
			this.transaction = transaction;
			if (this.transaction.getPerson() != null) {
				name = " - " + this.transaction.getPerson().getName();
			}
		}
		this.title = category.getText() + name;
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

	public List<Group2> getGroups() {
		return groups;
	}

	public void setGroups(List<Group2> groups) {
		this.groups = groups;
	}

}
