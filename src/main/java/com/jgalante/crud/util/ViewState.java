package com.jgalante.crud.util;

public class ViewState {

	public static final ViewState LIST = new ViewState(1, "List");
	public static final ViewState NEW = new ViewState(2, "New");
	public static final ViewState EDIT = new ViewState(3, "Edit");
	public static final ViewState REMOVE = new ViewState(4, "Remove");
	
	private Integer id;

	private String text;

	public ViewState(Integer id, String text) {
		super();
		this.id = id;
		this.text = text;
	}
	
	public ViewState(String text) {
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ViewState)) {
			return false;
		}
		ViewState other = (ViewState) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (text == null) {
			if (other.text != null) {
				return false;
			}
		} else if (!text.equals(other.text)) {
			return false;
		}
		return true;
	}

	

}
