package com.jgalante.crud.util;

import java.io.Serializable;

public class ColumnModel implements Serializable {

		private static final long serialVersionUID = 1L;
		
		private String header;
		private String property;
		private Object value;
		private boolean visible = false;
		
		public ColumnModel(String header) {
			super();
			this.header = header;
		}
		
		public ColumnModel(Object value) {
			super();
			this.value = value;
		}
		
		public ColumnModel(String header, Object value) {
			super();
			this.header = header;
			this.value = value;
		}
		
		public ColumnModel(String header, Object value, boolean visible) {
			super();
			this.header = header;
			this.value = value;
			this.visible = visible;
		}

		public ColumnModel(String header, String property, Object value) {
			super();
			this.header = header;
			this.property = property;
			this.value = value;
		}

		public String getHeader() {
			return header;
		}

		public String getProperty() {
			return property;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

		public boolean isVisible() {
			return visible;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((value == null) ? 0 : value.hashCode());
			return result;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ColumnModel other = (ColumnModel) obj;
			if (value == null) {
				if (other.value != null)
					return false;
			} else if (!value.equals(other.value))
				return false;
			return true;
		}
		
		

	}