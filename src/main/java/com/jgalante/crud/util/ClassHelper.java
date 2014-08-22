package com.jgalante.crud.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.el.PropertyNotFoundException;
import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Enumerated;

public class ClassHelper {

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
	 * Obtém a propriedade de uma classe que está anotado por: {@link Column}, {@link Basic}, 
	 * {@link Enumerated} ou {@link AttributeOverride}.
	 * 
	 * @param type tipo da classe pesquisada
	 * @param fieldName nome da propriedade
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
	 * @param fields lista com algumas propriedades
	 * @param type tipo da classe pesquisada
	 * @return lista com as novas propriedades encontradas
	 */
	public static List<Field> getAllFields(List<Field> fields, Class<?> type) {
	    for (Field field: type.getDeclaredFields()) {
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
				    if(t instanceof ParameterizedType){
				    	//http://stackoverflow.com/questions/182872/how-to-test-whether-method-return-type-matches-liststring
				    	
				    	 ParameterizedType pt = (ParameterizedType)t;
				         Type[] actualGenericParameters = pt.getActualTypeArguments();
				         for (int i = 0; i < actualGenericParameters.length; i++) {
				             if (actualGenericParameters[i] instanceof Class) {
//				                 System.out.println(((Class) actualGenericParameters[i]).getName());
				                 klass = (Class) actualGenericParameters[i];
				                 break;
				             } else {
				                 System.out.println("\tFailure generic parameter is not a class");
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

	public static String convertDateToString(Date date) {
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

		// Using DateFormat format method we can create a string 
		// representation of a date with the defined format.
		return df.format(date);
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
		calendar.add(Calendar.MONTH, numMonths);
		return calendar.getTime();
	}
}
