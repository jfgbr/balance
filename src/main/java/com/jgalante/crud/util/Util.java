package com.jgalante.crud.util;

import java.io.StringReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

import javax.el.PropertyNotFoundException;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Enumerated;

import org.hibernate.LazyInitializationException;

import com.jgalante.crud.entity.BaseEntity;

public class Util {

	public static Class<?> getClass(Class<?> parameterizedClass, Integer arg) {
		Class<?> argClass;
		ParameterizedType parameterizedType = (ParameterizedType) parameterizedClass
				.getGenericSuperclass();
		argClass = (Class<?>) parameterizedType.getActualTypeArguments()[arg];
		return argClass;
	}

	public static Type getSuperClassType(Type type) {
		return ((Class<?>) type).getGenericSuperclass();
	}

	public static Type[] getTypeArguments(Type type) {
		Type superClassType = getSuperClassType(type);
		Type[] typeArguments = null;
		try {
			typeArguments = ((ParameterizedType) (superClassType))
					.getActualTypeArguments();

		} catch (Exception e) {
			typeArguments = getTypeArguments(superClassType);
		}
		return typeArguments;
	}

	/**
	 * Obtém uma instância da classe com o valor passados por parametro.
	 * 
	 * @param value
	 *            o valor a ser convertido para classe
	 * @param type
	 *            a classe que receberá o valor
	 * @return a instância da classe passada
	 * @throws ClassCastException
	 *             se não for possível converter o valor para a classe
	 */
	public static Object convertIfNeeded(Object value, Class<?> type)
			throws ClassCastException {
		if (value == null)
			return value;

		if (type.isInstance(value))
			return value;

		if (isAssignableFrom(String.class, type)) {
			return value.toString();
		}

		if (isAssignableFrom(Boolean.class, type)) {
			return new Boolean(value.toString());
		}

		if (isAssignableFrom(Date.class, type)) {
			SimpleDateFormat simpleDateFormat;

			if (value.toString().contains(";")) {
				List<String> values = Arrays
						.asList(value.toString().split(";"));
				String data1 = values.get(0).toString().concat(" 00:00:00");
				String data2 = values.get(1).toString().concat(" 23:59:59");
				return data1.concat(";").concat(data2);
			} else {
				if (!value.toString().contains(":")) {
					value = value.toString().concat(" 00:00:00");
				}

				if (value.toString().contains("/")) {
					simpleDateFormat = new SimpleDateFormat(
							"MM/dd/yyyy HH:mm:ss");
				} else if (value.toString().contains("-")) {
					simpleDateFormat = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
				} else {
					simpleDateFormat = new SimpleDateFormat("ddMMyyyy HH:mm:ss");
				}
				try {
					return simpleDateFormat.parse(value.toString());
				} catch (ParseException e) {
					return null;
				}
			}

		}

		try {
			if (isAssignableFrom(Number.class, type)
					|| isAssignableFrom(Number.class, type.getSuperclass())) {
				// converte para o padrão de casas decimais
				String newValue = value.toString().replaceAll("/.", "")
						.replaceAll(",", ".");
				if (type.equals(Double.class)) {
					return new Double(newValue.toString());
				} else if (type.equals(Float.class)) {
					return new Float(newValue.toString());
				} else if (type.equals(Long.class)) {
					return new Long(newValue.toString());
				} else if (type.equals(Integer.class)) {
					return new Integer(newValue.toString());
				} else if (type.equals(Short.class)) {
					return new Short(newValue.toString());
				} else if (type.equals(BigInteger.class)) {
					return new BigInteger(newValue.toString());
				} else {

					return type.getConstructor(String.class).newInstance(
							newValue.toString());

				}
			}
		} catch (NumberFormatException e) {
		} catch (IllegalArgumentException e) {
		} catch (SecurityException e) {
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		} catch (NoSuchMethodException e) {
		}

		try {
			return value.toString();
		} catch (Exception e) {
			throw new ClassCastException("Unable to convert value of type "
					+ value.getClass().getName() + " to type " + type.getName());
		}
	}

	/**
	 * Identifica se a classe é uma instância da classe esperada
	 * 
	 * @param assignableClass
	 *            instância da classe
	 * @param expectedClass
	 *            classe esperada
	 * @return verdadeiro ou falso
	 */
	public static boolean isAssignableFrom(Class<?> assignableClass,
			Class<?> expectedClass) {
		return assignableClass.isAssignableFrom(expectedClass);
	}

	/**
	 * Obtém a propriedade de uma classe que está anotado por: {@link Column},
	 * {@link Basic}, {@link Enumerated} ou {@link AttributeOverride}.
	 * 
	 * @param type
	 *            tipo da classe pesquisada
	 * @param fieldName
	 *            nome da propriedade
	 * @return a instância {@link Field} da propriedade
	 */
	public static Field getField(Class<?> type, String fieldName) {
		final List<Field> fields = getAllFields(new LinkedList<Field>(), type);

		for (Field field : fields) {
			if (!field.isAnnotationPresent(Column.class)
					&& !field.isAnnotationPresent(Basic.class)
					&& !field.isAnnotationPresent(Enumerated.class)
					&& !field.isAnnotationPresent(AttributeOverride.class)) {
				continue;
			}

			try {
				field.setAccessible(true);
				if (field.getName().equals(fieldName)) {
					return field;
				}

			} catch (IllegalArgumentException e) {
				continue;

			}
		}
		return null;
	}

	/**
	 * Obtém todas as propriedades de uma classe.
	 * 
	 * @param fields
	 *            lista com algumas propriedades
	 * @param type
	 *            tipo da classe pesquisada
	 * @return lista com as novas propriedades encontradas
	 */
	public static List<Field> getAllFields(List<Field> fields, Class<?> type) {
		for (Field field : type.getDeclaredFields()) {
			fields.add(field);
		}

		if (type.getSuperclass() != null) {
			fields = getAllFields(fields, type.getSuperclass());
		}

		return fields;
	}

	@SuppressWarnings("rawtypes")
	public static Class<?> getExpectedClass(Class<?> rootClass,
			String propertyPath) throws PropertyNotFoundException {
		if (propertyPath == null || "".equals("propertyPath"))
			return rootClass;
		String[] chain = propertyPath.split("\\.");

		Class<?> klass = rootClass;

		for (String property : chain) {
			String getMethod = "get" + property.substring(0, 1).toUpperCase()
					+ property.substring(1);
			String isMethod = "is" + property.substring(0, 1).toUpperCase()
					+ property.substring(1);
			boolean found = false;

			for (Method method : klass.getMethods()) {
				if (method.getParameterTypes().length == 0
						&& (method.getName().equals(getMethod) || method
								.getName().equals(isMethod))) {
					klass = method.getReturnType();

					Type t = method.getGenericReturnType();
					if (t instanceof ParameterizedType) {
						// http://stackoverflow.com/questions/182872/how-to-test-whether-method-return-type-matches-liststring

						ParameterizedType pt = (ParameterizedType) t;
						Type[] actualGenericParameters = pt
								.getActualTypeArguments();
						for (int i = 0; i < actualGenericParameters.length; i++) {
							if (actualGenericParameters[i] instanceof Class) {
								// System.out.println(((Class)
								// actualGenericParameters[i]).getName());
								klass = (Class) actualGenericParameters[i];
								break;
							} else {
								System.out
										.println("\tFailure generic parameter is not a class");
							}
						}
					}

					found = true;
					break;
				}
			}
			if (!found)
				throw new PropertyNotFoundException("Could not find property '"
						+ propertyPath + "' on class " + rootClass + ".");
		}

		return klass;
	}

	public static Object getBeanByName(String name, BeanManager bm) {
		Bean<?> bean = bm.getBeans(name).iterator().next();
		CreationalContext<?> ctx = bm.createCreationalContext(bean);
		Object o = bm.getReference(bean, bean.getBeanClass(), ctx);
		return o;
	}

	public static Object getBeanByType(Type t, BeanManager bm) {
		Bean<?> bean = bm.getBeans(t).iterator().next();
		CreationalContext<?> ctx = bm.createCreationalContext(bean);
		Object o = bm.getReference(bean, bean.getBeanClass(), ctx);
		return o;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBeansReference(Class<T> beanClass, BeanManager bm,
			Annotation... qualifiers) {
		Bean<?> bean = bm.getBeans(beanClass).iterator().next();
		CreationalContext<?> ctx = bm.createCreationalContext(bean);
		Type beanType = beanClass == null ? bean.getBeanClass() : beanClass;
		InjectionPoint injectionPoint = new CustomInjectionPoint(bean,
				beanType, qualifiers);

		return (T) bm.getInjectableReference(injectionPoint, ctx);

		// Object o = bm.getReference(bean, bean.getBeanClass(), ctx);
		// return o;
	}

	static class CustomInjectionPoint implements InjectionPoint {

		private final Bean<?> bean;

		private final Type beanType;

		private final Set<Annotation> qualifiers;

		public CustomInjectionPoint(Bean<?> bean, Type beanType,
				Annotation... qualifiers) {
			this.bean = bean;
			this.beanType = beanType;
			this.qualifiers = new HashSet<Annotation>(Arrays.asList(qualifiers));
		}

		@Override
		public Type getType() {
			return this.beanType;
		}

		@Override
		public Set<Annotation> getQualifiers() {
			return this.qualifiers;
		}

		@Override
		public Bean<?> getBean() {
			return this.bean;
		}

		@Override
		public Member getMember() {
			return null;
		}

		@Override
		public boolean isDelegate() {
			return false;
		}

		@Override
		public boolean isTransient() {
			return false;
		}

		@Override
		public Annotated getAnnotated() {
			return new Annotated() {

				@Override
				public Type getBaseType() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public Set<Type> getTypeClosure() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				@SuppressWarnings("unchecked")
				public <T extends Annotation> T getAnnotation(
						Class<T> annotationType) {
					T result = null;

					for (Annotation annotation : getAnnotations()) {
						if (annotation.annotationType() == annotationType) {
							result = (T) annotation;
							break;
						}
					}

					return result;
				}

				@Override
				public Set<Annotation> getAnnotations() {
					return qualifiers;
				}

				@Override
				public boolean isAnnotationPresent(
						Class<? extends Annotation> annotationType) {
					return qualifiers.contains(annotationType);
				}
			};
		}
	}

	/**
	 * <p>
	 * Replaces the numbers between braces in the given string with the given
	 * parameters. The process will replace a number between braces for the
	 * parameter for which its order in the set of parameters matches with the
	 * number of the given string.
	 * <p>
	 * For exemple, if is received the following string
	 * "Treats an {0} exception" and the set of parameters
	 * {"DemoiselleException"}, the return will be the following string:
	 * "Treats an DemoiselleException exception".
	 * 
	 * @param string
	 *            with the numbers with braces to be replaced with the
	 *            parameters.
	 * @param params
	 *            parameters that will replace the number with braces in the
	 *            given string.
	 * @return String string with numbers replaced with the matching parameter.
	 */
	public static String getString(final String string, final Object... params) {
		String result = null;

		if (string != null) {
			result = new String(string);
		}

		if (params != null && string != null) {
			for (int i = 0; i < params.length; i++) {
				if (params[i] != null) {
					result = result.replaceAll("\\{" + i + "\\}",
							Matcher.quoteReplacement(params[i].toString()));
				}
			}
		}

		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected static <T extends BaseEntity> JsonObjectBuilder createJsonObjectBuilde(
			Class<T> entityClass, T entity) throws InstantiationException,
			IllegalAccessException {
		JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
		List<Field> fields = new ArrayList<Field>();
		fields = getAllFields(fields, entityClass);
		if (entity == null) {
//			entity = entityClass.newInstance();
			return jsonBuilder;
		}
		for (Field field : fields) {
			try {
				if (isAssignableFrom(BaseEntity.class, field.getType())) {
					BaseEntity baseEntity = (BaseEntity)runGetter(field, entity);
					if (baseEntity == null) {
						jsonBuilder.addNull(field.getName());
					} else {
						jsonBuilder.add(field.getName(),
							createJsonObjectBuilde((Class<BaseEntity>)field.getType(), (BaseEntity) runGetter(field, entity)));
					}
				} else if (isAssignableFrom(List.class, field.getType()) || isAssignableFrom(Set.class, field.getType())){
					Class paramClass = (Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
					if (isAssignableFrom(BaseEntity.class, paramClass)) {
						Collection<BaseEntity> list = (Collection<BaseEntity>) runGetter(field, entity);
						if (list == null || list.size() == 0) {
							jsonBuilder.addNull(field.getName());
						} else {
							for (BaseEntity object : list) {
								jsonBuilder.add(field.getName(),
										createJsonObjectBuilde(paramClass, object));
							}
						}
					}
					
				} else {
					Object value = runGetter(field, entity);
					if (value == null) {
						jsonBuilder.addNull(field.getName());
					} else {
						jsonBuilder.add(field.getName(), value.toString());// field.get(entity));
					}
				}
			} catch (LazyInitializationException | InvocationTargetException e) {
				continue;
			} catch (IllegalAccessException | IllegalArgumentException
					 e) {
				e.printStackTrace();
			}
		}
		return jsonBuilder;
	}

	public static <T extends BaseEntity> String writeJson(Class<T> entityClass,T entity) {
		try {
			return createJsonObjectBuilde(entityClass, entity).build().toString();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static <T extends BaseEntity> T readJson(Class<T> entityClass,
			String jsonTxt) {
		StringReader sr = new StringReader(jsonTxt);
		JsonReader jsonReader = Json.createReader(sr);
		// get JsonObject from JsonReader
		JsonObject jsonObject = jsonReader.readObject();

		// we can close IO resource and JsonReader now
		jsonReader.close();

		return readJsonObject(entityClass, jsonObject);
	}

	@SuppressWarnings("unchecked")
	public static <T extends BaseEntity> T readJsonObject(
			Class<T> entityClass, JsonObject jsonObject) {
		try {
			if (jsonObject.size() > 0) {
				T entity = entityClass.newInstance();
				List<Field> fields = new ArrayList<Field>();
				fields = getAllFields(fields, entity.getClass());
				for (Field field : fields) {
					try {
						if (isAssignableFrom(BaseEntity.class, field.getType())) {
								runSetter(field, entity, readJsonObject((Class<T>) field.getType(),
												jsonObject.getJsonObject(field.getName())));
							
		//					field.set(
		//							entity,
		//							readJsonObject((Class<T>) field.getType(),
		//									jsonObject.getJsonObject(field.getName())));
						} else {
							runSetter(field, entity, convertIfNeeded(jsonObject.getString(field.getName()),field.getType()));
						}
					} catch (ClassCastException | NullPointerException | IllegalArgumentException
							| InvocationTargetException e) {
	//					e.printStackTrace();
					}
				}
				return entity;
			}
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T extends BaseEntity> Object runGetter(Field field, T o)
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		if (o != null) {
			for (Method method : o.getClass().getMethods()) {
				if ((method.getName().startsWith("get") || method.getName()
						.startsWith("is"))
						&& (method.getName().length() == (field.getName()
								.length() + 3))) {
					if (method.getName().toLowerCase()
							.endsWith(field.getName().toLowerCase())) {
						return method.invoke(o);
					}
				}
			}
		}
		return null;
	}
	
	public static <T extends BaseEntity> void runSetter(Field field, T o, Object value)
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		if (o != null) {
			for (Method method : o.getClass().getMethods()) {
				if ((method.getName().startsWith("set"))
						&& (method.getName().length() == (field.getName()
								.length() + 3))) {
					if (method.getName().toLowerCase()
							.endsWith(field.getName().toLowerCase())) {
						method.invoke(o, value);
						break;
					}
				}
			}
		}
	}

	public static String convertDateToString(Date date) {
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

		// Using DateFormat format method we can create a string
		// representation of a date with the defined format.
		return df.format(date);
	}

	public static Calendar convertDateToCalendar(Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return calendar;
	}
	
	public static Calendar addDaystoCalendar(Calendar calendar, Integer numDays) {
		Calendar newCalendar = (Calendar) calendar.clone();
		newCalendar.add(Calendar.DAY_OF_MONTH, numDays);
		return newCalendar;
	}

	public static Date addDaystoDate(Calendar calendar, Integer numDays) {
		Calendar newCalendar = (Calendar) calendar.clone();
		newCalendar.add(Calendar.DAY_OF_MONTH, numDays);
		return newCalendar.getTime();
	}

	public static Date addDaystoDate(Date date, Integer numDays) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return addDaystoDate(calendar, numDays);
	}

	public static Date addMonthstoDate(Date date, Integer numMonths) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return addMonthstoCalendar(calendar, numMonths).getTime();
	}

	public static Calendar addMonthstoCalendar(Calendar calendar,
			Integer numMonths) {
		calendar.add(Calendar.MONTH, numMonths);
		return calendar;
	}

	public static Date subtractMonthstoDate(Date date, Integer numMonths) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return subtractMonthstoCalendar(calendar, numMonths).getTime();
	}

	public static Calendar subtractMonthstoCalendar(Calendar calendar,
			Integer numMonths) {
		calendar.add(Calendar.MONTH, -numMonths);
		return calendar;
	}

	public static Calendar beginOfMonth(Calendar calendar) {
		Calendar minDate = (Calendar)calendar.clone();
		minDate.set(Calendar.HOUR_OF_DAY, 0);
		minDate.set(Calendar.MINUTE, 0);
		minDate.set(Calendar.SECOND, 0);
		minDate.set(Calendar.DAY_OF_MONTH, minDate.getActualMinimum(Calendar.DAY_OF_MONTH));
		return minDate;
	}
	
	public static Calendar endOfMonth(Calendar calendar) {
		Calendar maxDate = (Calendar)calendar.clone();
		maxDate.set(Calendar.HOUR_OF_DAY, 23);
		maxDate.set(Calendar.MINUTE, 59);
		maxDate.set(Calendar.SECOND, 59);
		maxDate.set(Calendar.DAY_OF_MONTH, maxDate.getActualMaximum(Calendar.DAY_OF_MONTH));		
		return maxDate;
	}

	public static LinkedList<ColumnModel> months() {
		return months(0,11);
	}
	
	public static LinkedList<ColumnModel> months(Calendar startDate, Calendar endDate) {
		return months(getMonth(startDate), getMonth(endDate));
	}

	public static LinkedList<ColumnModel> months(Date startDate, Date endDate) {
    	return months(getMonth(startDate), getMonth(endDate));
	}

	public static LinkedList<ColumnModel> months(int monthStart, int monthEnd) {
		String[] months = new DateFormatSymbols().getMonths();
		LinkedList<ColumnModel> dates = new LinkedList<ColumnModel>();
		
    	boolean visible;
    	if (monthEnd == 0) monthEnd = 11;
    	
	    for (int i = monthStart; i < monthEnd+1; i++) {
	    	visible = false;
	    	if (monthStart <= i && monthEnd >= i) {
	    		visible = true;
	    	}
	    	Calendar date = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), i, 1, 0, 0, 0);
	    	dates.add(new ColumnModel(months[i], date, visible));
	    }
	    return dates;
	}
	
	public static LinkedList<Calendar> calendarMonths(int monthStart, int monthEnd) {
		LinkedList<Calendar> dates = new LinkedList<Calendar>();
		
    	if (monthEnd == 0) monthEnd = 11;
    	
	    for (int i = monthStart; i < monthEnd+1; i++) {
	    	Calendar date = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), i, 1, 0, 0, 0);
	    	dates.add(date);
	    }
	    return dates;
	}

	public static int getMonth(Date date) {
		if (date == null) {
			return 0;
		}
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return getMonth(calendar);
	}

	public static int getMonth(Calendar calendar) {
		if (calendar == null) {
			return 0;
		}
		int month;
		month = calendar.get(Calendar.MONTH);
		return month;
	}


	public static LinkedList<Integer> years() {
		LinkedList<Integer> years = new LinkedList<Integer>();
		Integer current = Calendar.getInstance().get(Calendar.YEAR); 
		for (int i = 0; i < 10; i++,--current) {
			years.add(current);
		}
		return years;
	}
}
