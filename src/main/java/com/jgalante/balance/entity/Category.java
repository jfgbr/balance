package com.jgalante.balance.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.jgalante.jgcrud.entity.BaseEntity;

@Entity
public class Category extends BaseEntity {

	@Column(name = "text", nullable = false)
	private String text;

	@JoinColumn(name = "id_parent")
	@ManyToOne(fetch = FetchType.EAGER)
	private Category parent;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "parent")
	private Set<Category> subCategories;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "category")
	private Set<Transaction> transactions;
	
	@Transient
	private Integer level = 1;
	
	public Category() {
		super();
	}

	public Category(String text, Category parent) {
		super();
		this.text = text;
		this.parent = parent;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	public Set<Category> getSubCategories() {
		return subCategories;
	}

	public void setSubCategories(Set<Category> subCategories) {
		this.subCategories = subCategories;
	}

	public Set<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(Set<Transaction> transactions) {
		this.transactions = transactions;
	}
	
	public Integer getLevel() {
		if (parent != null && level == 1) {
			level += parent.getLevel();
		}
		return level;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (parent != null) {
			sb.append(parent.toString());
			sb.append(" > ");
		}
		sb.append(this.getText());
		return sb.toString();
	}
}
