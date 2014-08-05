package com.jgalante.balance.util;

import java.io.Serializable;
import java.math.BigDecimal;

public class ColumnModel implements Serializable {

		private static final long serialVersionUID = 1L;
		
		private String header;
		private String property;
		private BigDecimal value;
		
		public ColumnModel(String header) {
			super();
			this.header = header;
		}
		
		public ColumnModel(String header, BigDecimal value) {
			super();
			this.header = header;
			this.value = value;
		}
		
		public ColumnModel(String header, String property, BigDecimal value) {
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

		public BigDecimal getValue() {
			return value;
		}

		public void setValue(BigDecimal value) {
			this.value = value;
		}

	}