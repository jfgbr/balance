package com.jgalante.balance.entity;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import com.jgalante.crud.entity.BaseEntity;

@Entity(name = "category")
public class Category extends BaseEntity {

	@Column(name = "ds_text", nullable = false)
	private String text;

	@JoinColumn(name = "id_parent")
	// @ManyToOne(fetch = FetchType.EAGER)
	@ManyToOne(fetch = FetchType.LAZY)
	private Category parent;

	@JoinColumn(name = "id_person")
	// @ManyToOne(fetch = FetchType.EAGER)
	@ManyToOne(fetch = FetchType.LAZY)
	private Person person;

	// @OneToMany(fetch = FetchType.EAGER, mappedBy = "parent")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
	@OrderBy("nr_order ASC")
	private Set<Category> subCategories;

	// @OneToMany(fetch = FetchType.EAGER, mappedBy = "category")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
	private Set<Transaction> transactions;

	@Column(name = "is_positive", nullable = false)
	private Boolean positive = false;

	@Column(name = "nr_order", nullable = false)
	private Integer order = 1;

	@Transient
	private Integer level = 1;

	public Category() {
		super();
	}

	public Category(Category parent, Category subCategories,
			Transaction transactions) {
		super();
		this.parent = parent;
		this.subCategories = new LinkedHashSet<Category>();
		this.subCategories.add(subCategories);
		this.transactions = new LinkedHashSet<Transaction>();
		this.transactions.add(transactions);
	}



	public Category(String text) {
		super();
		this.text = text;
	}

	public Category(String text, Category parent) {
		super();
		this.text = text;
		this.parent = parent;
	}

	public Category(String text, Set<Category> subCategories) {
		super();
		this.text = text;
		this.subCategories = subCategories;
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

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Boolean getPositive() {
		return positive;
	}

	public void setPositive(Boolean positive) {
		this.positive = positive;
	}

	public Integer getLevel() {
		if (parent != null && level == 1) {
			level += parent.getLevel();
		}
		return level;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
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

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			Category other = (Category) obj;
			if (this.text == null) {
				if (other.text != null) {
					return false;
				}
			} else if (!this.text.equals(other.text)) {
				return false;
			}
			if (this.person == null) {
				if (other.person != null) {
					return false;
				}
			} else if (!this.person.equals(other.person)) {
				return false;
			}
		}
		return true;
	}
}
