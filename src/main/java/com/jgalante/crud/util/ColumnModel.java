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

	}