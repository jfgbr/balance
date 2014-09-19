package com.jgalante.crud.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query;

import com.jgalante.crud.util.Filter.Operator;

public class QueryParam implements Serializable {

	private static final long serialVersionUID = 4585134761403626269L;

	protected int rowCount;
	private String rootAlias = "obj";
	private String uniqueId = "id";

	private String fromClause = "";
	private String whereClause = "";

	private Map<String, Boolean> orderBy;

	private Map<String, String> alias = new LinkedHashMap<String, String>();
	
	private Map<String, String> aliasParameter = new LinkedHashMap<String, String>();

	private boolean includeJoins = true;

	private Set<Filter> filters;

	private List<Filter> joinFilters;
	
	private List<Filter> groupByFilters;

	private Class<?> entityClass;

	private boolean distinct;
	
	private boolean firstFilter = true;

	public QueryParam(boolean includeJoins, Class<?> entityClass) {
		super();
		this.includeJoins = includeJoins;
		this.entityClass = entityClass;
	}

	public QueryParam(Class<?> entityClass) {
		super();
		this.entityClass = entityClass;
	}

	public QueryParam(String rootAlias, Class<?> entityClass) {
		super();
		this.rootAlias = rootAlias;
		this.entityClass = entityClass;
	}

	public QueryParam(String rootAlias, List<Filter> joinFilters,
			Class<?> entityClass) {
		super();
		this.rootAlias = rootAlias;
		this.joinFilters = joinFilters;
		this.entityClass = entityClass;
	}

	public void reset() {
		filters = null;
		joinFilters = null;
		groupByFilters = null;
		orderBy = null;
		fromClause = "";
		whereClause = "";
		includeJoins = true;
		alias = new LinkedHashMap<String, String>();
		aliasParameter = new LinkedHashMap<String, String>();
	}
	
	public List<Filter> createListFiltersFromMap(Map<String, Object> filters) {
		List<Filter> listFilters = new LinkedList<Filter>();
		listFilters = defineFilters(filters);
		return listFilters;
	}

	protected List<Filter> defineFilters(Map<String, Object> filters) {

		List<Filter> listFilters = new ArrayList<Filter>();
		Class<?> originalClass;
		for (Map.Entry<String, Object> filter : filters.entrySet()) {
			originalClass = Util.getExpectedClass(entityClass,
					filter.getKey());
			Filter itemFilter = defineFilter(originalClass, filter.getKey(),
					filter.getValue());

			listFilters.add(itemFilter);
		}
		return listFilters;
	}

	public Filter defineFilter(Class<?> originalClass, String key, Object value) {

		Filter itemFilter = new Filter(key, Util.convertIfNeeded(value,
				originalClass));

		if (itemFilter.getValue() == null) {
			itemFilter.setOp(Filter.Operator.IS_NULL);
		} else {
			List<String> values = Arrays.asList(itemFilter.getValue()
					.toString().split(";"));
			if (values.size() > 1) {
				if (Util.isAssignableFrom(originalClass, Date.class)) {
					itemFilter.setOp(Filter.Operator.BETWEEN);
					itemFilter.setFilters(new ArrayList<Filter>());

					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
							"MM/dd/yyyy HH:mm:ss");

					Date minDate;
					Date maxDate;
					try {
						minDate = (Date) simpleDateFormat.parse(values.get(0)
								.toString());
						maxDate = (Date) simpleDateFormat.parse(values.get(1)
								.toString());
						itemFilter.getFilters().add(new Filter(key, minDate));
						itemFilter.getFilters().add(new Filter(key, maxDate));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					itemFilter.setOp(Filter.Operator.IN);
					itemFilter.setFilters(new ArrayList<Filter>());
					for (String itemValue : values) {
						itemFilter.getFilters().add(
								new Filter(itemFilter.getKey(), Util
										.convertIfNeeded(itemValue,
												originalClass)));
					}
				}
			} else if (Util.isAssignableFrom(originalClass,
					Boolean.class)) {
				if (new Boolean(itemFilter.getValue().toString())) {
					itemFilter.setOp(Filter.Operator.IS_TRUE);
				} else {
					itemFilter.setOp(Filter.Operator.IS_FALSE);
				}
			} else if (Util.isAssignableFrom(Enum.class, originalClass)) {
				itemFilter.setOp(Filter.Operator.EQUAL);
			} else if (Util.isAssignableFrom(originalClass, Date.class)) {

				itemFilter.setOp(Filter.Operator.BETWEEN);
				itemFilter.setFilters(new ArrayList<Filter>());

				Calendar minDate = new GregorianCalendar();
				minDate.setTime((Date) itemFilter.getValue());
				minDate.set(Calendar.HOUR_OF_DAY, 0);
				minDate.set(Calendar.MINUTE, 0);
				minDate.set(Calendar.SECOND, 0);
				Calendar maxDate = Util.addDaystoCalendar(minDate, 1);

				itemFilter.getFilters().add(new Filter(key, minDate.getTime()));
				itemFilter.getFilters().add(new Filter(key, maxDate.getTime()));

			} else if (Util
					.isAssignableFrom(originalClass, String.class)) {
				itemFilter.setOp(Filter.Operator.LIKE);
				itemFilter.setValue("%"
						+ itemFilter.getValue().toString().toLowerCase() + "%");

			} else if (Util.isAssignableFrom(originalClass,
					BigInteger.class)) {

				itemFilter.setValue(Util.convertIfNeeded(
						itemFilter.getValue(), originalClass));

			} else if (Util.isAssignableFrom(originalClass,
					BigDecimal.class)) {

				itemFilter.setValue(Util.convertIfNeeded(
						itemFilter.getValue(), originalClass));

				Double minNum = Double.parseDouble(itemFilter.getValue()
						.toString());
				Double maxNum = new Double(minNum.intValue() + 0.9999);

				// Se o número for inteiro, aplico esse tipo de filtro
				if (((minNum % 1) == 0)) {
					itemFilter.setOp(Filter.Operator.BETWEEN);
					itemFilter.setFilters(new ArrayList<Filter>());

					itemFilter.getFilters().add(
							new Filter(key, new BigDecimal(minNum)));
					itemFilter.getFilters().add(
							new Filter(key, new BigDecimal(maxNum)));
				}

			}

		}
		return itemFilter;
	}

	public void addFilters(List<Filter> filters) {
		if (filters != null) {
			if (this.filters == null) {
				this.filters = new LinkedHashSet<Filter>();
			}
			getFilters().addAll(filters);
		}
	}
	
	public void addFilter(Filter filter) {
		if (filters == null) {
			filters = new LinkedHashSet<Filter>();
		}
		getFilters().add(filter);
	}

	public String generateAlias(String root, String key) {

		String[] parts = (key + ".").split("\\.");
		String tmpRoot = (!root.isEmpty() ? root + "." : "");
		String newAlias = "";// = root;//(!root.isEmpty()?root:"");
		for (String part : parts) {
			// newAlias = tmpRoot + part;
			// if (!newAlias.isEmpty()) {
			// newAlias += ".";
			// }
			newAlias = alias.get(tmpRoot + part);

			Integer idCount = alias.size();
			if (newAlias == null) {
				StringBuilder sb = new StringBuilder();
				sb.append("alias_");
				sb.append(idCount.toString());
				newAlias = sb.toString();
				// if (root != null && !root.isEmpty()) {
				// sb.append(root).append(".");
				// }
				alias.put(tmpRoot + part, newAlias);
				tmpRoot = newAlias + ".";
			}

		}
		return newAlias;
	}

	public String generateAliasParameter(String key) {
		String newAliasParameter = null;//aliasParameter.get(key);
		Integer idCount = aliasParameter.size();
		if (newAliasParameter == null) {
			StringBuilder sb = new StringBuilder("param_");
			sb.append(idCount.toString());
			newAliasParameter = sb.toString();
			aliasParameter.put(String.format(
					"%s_%s",key,newAliasParameter), newAliasParameter);
		}
		return newAliasParameter;
	}

	private String findAlias(String key) {
		String property = (key.contains(rootAlias) ? key : String.format(
				"%s.%s", rootAlias, key));
		int pos = property.lastIndexOf('.');
		if (pos == -1) {
			return null;
		}
		String currentKey = property.substring(0, pos);
		if (currentKey.equals(rootAlias)) {
			return property;
		}
		String newAlias = getAlias().get(currentKey);
		if (newAlias == null) {
			newAlias = findAlias(currentKey);
		}
		newAlias = String.format("%s.%s", newAlias,
				property.substring(pos + 1, property.length()));
		return newAlias;
	}

	private String createAliasJoin(String path) {
//		String tmpAlias = path;
//		if (!path.contains(rootAlias)) {
//			tmpAlias = String.format("%s.%s", rootAlias, path);
//		}
//		String[] parts = (tmpAlias).split("\\.");
//
//		String newAlias = null;
//		String key = rootAlias;
//
//		for (String part : parts) {
//			if (!rootAlias.equals(part)) {
//				key = String.format("%s.%s", key, part);
//				tmpAlias = String.format("%s.%s", tmpAlias, part);
//				newAlias = getAlias().get(tmpAlias);
//				if (newAlias != null) {
//					tmpAlias = newAlias;
//				} else {
//					newAlias = String.format("alias_%s", getAlias().size());
//					getAlias().put(key, newAlias);
//				}
//			} else {
//				tmpAlias = rootAlias;
//			}
//		}
//		return String.format("%s %s", tmpAlias, newAlias);
		String tmpAlias = path;
		if (!path.contains(rootAlias)) {
			tmpAlias = String.format("%s.%s", rootAlias, path);
		}
		String newAlias = getAlias().get(tmpAlias);
		if (newAlias == null) {
			newAlias = findAlias(tmpAlias);
			newAlias = (newAlias == null) ? tmpAlias : newAlias;
			tmpAlias = newAlias;
			newAlias = String.format("alias_%s", getAlias().size());
			getAlias().put(String.format("%s.%s", rootAlias, path), newAlias);
		}
		return String.format("%s %s", tmpAlias, newAlias);
	}

	public String createSearchJPQL() {
		String select = generateSelectClause();
		String from = generateFromClause();
		String where = getWhereClause();
		String groupBy = generateGroupByClause();
		String orderBy = generateOrderByClause();

		StringBuilder sb = new StringBuilder();
		sb.append(select);
		sb.append(" ");
		sb.append(from);
		sb.append(" ");
		sb.append(where);
		sb.append(" ");
		sb.append(groupBy);
		sb.append(" ");
		sb.append(orderBy);
		return sb.toString();
	}


	public String createCountJPQL() {
		String where = getWhereClause();
		String from = generateFromClause().replaceAll("FETCH ", "");

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT COUNT(*");
		sb.append(") ");
		sb.append(from);
		sb.append(" ");
		sb.append(where);

		return sb.toString();
	}


	protected String generateSelectClause() {
		StringBuilder sb = new StringBuilder("SELECT ");
		if (distinct) {
			sb.append("DISTINCT ");
		}
		sb.append(getRootAlias());

		return sb.toString();
	}


	protected String generateWhereClause() {

		// if (!getWhereClause().isEmpty()) {
		// return getWhereClause();
		// }

		StringBuilder sb = new StringBuilder();
		firstFilter = true;
		if (getFilters() != null) {
			LinkedList<Filter> tmpFilters = new LinkedList<Filter>(getFilters());
			for (Filter filter : tmpFilters) {
				if (filter != null) {
					if (!Operator.JOIN.equals(filter.getOp())) {
						if (firstFilter) {
							sb.append("WHERE ");
						}
						sb.append(createWhere(filter));
						firstFilter = false;
					} else {
						addJoinFilter(filter);
						getFilters().remove(filter);
					}
				}
			}
		}

		setWhereClause(sb.toString());

		return sb.toString();
	}



	private String generateOrderByClause() {
		StringBuilder sb = null;
		boolean first = true;

		if (getOrderBy() != null) {
			for (Map.Entry<String, Boolean> order : getOrderBy().entrySet()) {
				if (first) {
					sb = new StringBuilder(" ORDER BY ");
					first = false;
				} else {
					sb.append(", ");
				}
				sb.append(findAlias(order.getKey()));
				sb.append(order.getValue() ? " DESC" : " ASC");
			}
		}

		if (first) {
			return "";
		}

		return sb.toString();
	}


	private String generateFromClause() {
		if (!getFromClause().isEmpty()) {
			return getFromClause();
		}
		StringBuilder sb = new StringBuilder(" FROM ");
		sb.append(this.entityClass.getName()).append(" ")
				.append(getRootAlias());

		sb.append(generateJoinClause());

		// Salva para usar na query do contador
		setFromClause(sb.toString());

		return sb.toString();
	}


	private String generateJoinClause() {
		StringBuilder sb = new StringBuilder();
		if (joinFilters != null) {
			for (Filter filter : joinFilters) {
				String alias = createAliasJoin(filter.getKey());
				sb.append(" LEFT JOIN FETCH ");
				sb.append(alias);
			}
		}

		return sb.toString();
	}

	private String generateGroupByClause() {

		StringBuilder sb = new StringBuilder();
		if (groupByFilters != null) {
			boolean first = true;
			for (Filter filter : groupByFilters) {
				if (first) {
					sb.append(" GROUP BY ");
					first = false;
				} else {
					sb.append(",");
				}
				String alias = findAlias(filter.getKey());
				sb.append(alias);
			}
		}

		return sb.toString();
	}

	public String createWhere(Filter filter) {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		Integer count = 0;
		try {
			switch (filter.getOp()) {
			case AND:
//				first = true;
				sb.append(" ( ");
				for (Filter filterItem : filter.getFilters()) {
					if (!firstFilter) {
						sb.append(" AND ");
					} else {
						firstFilter = false;
					}
					sb.append(createWhere(filterItem));
				}
				sb.append(" ) ");

				break;

			case OR:
//				first = true;
				sb.append(" ( ");
				for (Filter filterItem : filter.getFilters()) {
					if (!firstFilter) {
						sb.append(" OR ");
					} else {
						firstFilter = false;
					}
					sb.append(createWhere(filterItem));
				}
				sb.append(" ) ");
				break;

			case BETWEEN:
				sb.append(" ( ");
				filter.setAlias(generateAliasParameter(filter.getKey()));

				writeAlias(filter, sb);
				sb.append(" BETWEEN ");
				for (Filter filterItem : filter.getFilters()) {
					if (!first) {
						sb.append(" AND ");
					} else {
						first = false;
					}
					filterItem.setAlias(filter.getAlias() + "_" + count++);
					writeAliasParameter(filterItem, sb);
				}
				sb.append(" ) ");
				break;

			case LIKE:
				filter.setAlias(generateAliasParameter(filter.getKey()));
				sb.append(" LCASE(");
				writeAlias(filter, sb);
				sb.append(") like ");
				writeAliasParameter(filter, sb);
				break;

			case EQUAL:
				filter.setAlias(generateAliasParameter(filter.getKey()));
				writeAlias(filter, sb);
				sb.append(" = ");
				writeAliasParameter(filter, sb);
				break;

			case NOT_EQUAL:
				filter.setAlias(generateAliasParameter(filter.getKey()));
				writeAlias(filter, sb);
				sb.append(" != ");
				writeAliasParameter(filter, sb);
				break;

			case IN:
				filter.setAlias(generateAliasParameter(filter.getKey()));
				writeAlias(filter, sb);
				sb.append(" IN ( ");
				count = 0;
				first = true;
				for (Filter filterItem : filter.getFilters()) {
					if (!first) {
						sb.append(",");
					} else {
						first = false;
					}
					filterItem.setAlias(filter.getAlias() + "_" + count++);
					writeAliasParameter(filterItem, sb);
				}
				sb.append(" ) ");
				break;

			case NOT_IN:
				filter.setAlias(generateAliasParameter(filter.getKey()));
				writeAlias(filter, sb);
				sb.append(" NOT IN ( ");
				count = 0;
				first = true;
				for (Filter filterItem : filter.getFilters()) {
					if (!first) {
						sb.append(",");
					} else {
						first = false;
					}
					filterItem.setAlias(filter.getAlias() + "_" + count++);
					writeAliasParameter(filterItem, sb);
				}
				sb.append(" ) ");
				break;
			
			case EQUAL_GREATER:
				filter.setAlias(generateAliasParameter(filter.getKey()));
				writeAlias(filter, sb);
				sb.append(" >= ");
				writeAliasParameter(filter, sb);
				break;
			
			case EQUAL_LESS:
				filter.setAlias(generateAliasParameter(filter.getKey()));
				writeAlias(filter, sb);
				sb.append(" <= ");
				writeAliasParameter(filter, sb);
				break;

			case IS_NULL:
				writeAlias(filter, sb);
				sb.append(" IS NULL ");
				break;

			case IS_NOT_NULL:
				writeAlias(filter, sb);
				sb.append(" IS NOT NULL ");
				break;

			case IS_TRUE:
				writeAlias(filter, sb);
				sb.append(" = 1 ");
				break;

			case IS_FALSE:
				writeAlias(filter, sb);
				sb.append(" = 0 ");
				break;

			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	private void writeAlias(Filter filter, StringBuilder sb) {
		sb.append(findAlias(filter.getKey()));
	}
	

	private void writeAliasParameter(Filter filter, StringBuilder sb) {
		sb.append(":");
		sb.append(filter.getAlias());
		sb.append(" ");
	}

	public void updateParameter(Query query) {
		updateParameter(query, getFilters());
	}

	public void updateParameter(Query query, Set<Filter> filters) {
		for (Filter filter : filters) {
			updateParameter(query, filter);
		}
	}

	public void updateParameter(Query query, Filter filter) {
		if (filter.getFilters() == null) {
			if (filter.getValue() != null && filter.getAlias() != null) {
				query.setParameter(filter.getAlias(), filter.getValue());
			}
		} else {
			updateParameter(query, new LinkedHashSet<Filter>(filter.getFilters()));
		}
	}

	public void addJoinFilters(List<String> strJoinFilters) {
		if (strJoinFilters != null) {
			for (String joinFilter : strJoinFilters) {
				addJoinFilter(joinFilter);
			}
		}
	}

	public void addJoinFilter(String joinFilter) {
		addJoinFilter(new Filter(joinFilter, null, Operator.JOIN));
	}
	
	public void addJoinFilter(Filter filter) {
		if (joinFilters == null) {
			joinFilters = new LinkedList<Filter>();
		}
		joinFilters.add(filter);
	}
	
	public void addGroupByFilter(String groupByFilter) {
		if (groupByFilters == null) {
			groupByFilters = new LinkedList<Filter>();
		}
		List<String> filters = new LinkedList<String>(Arrays.asList(groupByFilter
				.split(",")));
		for (String strFilter : filters) {
			groupByFilters.add(new Filter(strFilter, null, Operator.GROUPBY));
		}
	}
	
	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public String getRootAlias() {
		return rootAlias;
	}

	public void setRootAlias(String rootAlias) {
		this.rootAlias = rootAlias;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getFromClause() {
		return fromClause;
	}

	public void setFromClause(String fromClause) {
		this.fromClause = fromClause;
	}

	public String getWhereClause() {
		// if (whereClause == null || whereClause.isEmpty()) {
		whereClause = generateWhereClause();
		// }
		return whereClause;
	}

	public void setWhereClause(String whereClause) {
		this.whereClause = whereClause;
	}

	public Map<String, Boolean> getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Map<String, Boolean> orderBy) {
		this.orderBy = orderBy;
	}

	public Set<Filter> getFilters() {
		return filters;
	}

	public void setFilters(Set<Filter> filters) {
		this.filters = filters;
	}

	public Map<String, String> getAlias() {
		return alias;
	}

	public Map<String, String> getAliasParameter() {
		return aliasParameter;
	}

	public List<Filter> getJoinFilters() {
		return joinFilters;
	}

	public void setJoinFilters(List<Filter> joinFilters) {
		this.joinFilters = joinFilters;
	}

	public boolean isIncludeJoins() {
		return includeJoins;
	}

	public void setIncludeJoins(boolean includeJoins) {
		this.includeJoins = includeJoins;
	}

	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

}
