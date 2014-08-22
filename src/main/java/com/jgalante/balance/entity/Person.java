package com.jgalante.balance.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.jgalante.crud.entity.BaseEntity;


@Entity(name = "person")
public class Person extends BaseEntity {

	@Column(name = "ds_name", nullable = false)
	private String name;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "person")
	private Set<Category> categories;

	public Person() {
		super();
	}

	public Person(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			Person other = (Person) obj;
			if (this.name == null) {
				if (other.name != null) {
					return false;
				}
			} else if (!this.name.equals(other.name)) {
				return false;
			}
		}
		return true;
	}

}
