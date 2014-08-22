package com.jgalante.crud.util;

public class ViewState {

	public static final ViewState LIST = new ViewState(0, "List");
	public static final ViewState NEW = new ViewState(1, "New");
	public static final ViewState EDIT = new ViewState(2, "Edit");
	public static final ViewState REMOVE = new ViewState(3, "Remove");

	private Integer id;

	private String text;

	public ViewState(Integer id, String text) {
		super();
		this.id = id;
		this.text = text;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public boolean isList(){
		return this.equals(LIST);
	}
	
	public boolean isNew(){
		return this.equals(NEW);
	}
	
	public boolean isEdit(){
		return this.equals(EDIT);
	}
	
	public boolean isRemove(){
		return this.equals(REMOVE);
	}
	
	public void setNew(boolean value) {
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		ViewState other = (ViewState) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
