package com.jgalante.crud.converter;

import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.jgalante.crud.entity.BaseEntity;


@FacesConverter("entityConverter")
public class EntityConverter implements Converter {

	private static final String key = "com.jgalante.EntityConverter";
	private static final String empty = "";

	private Map<String, Object> getViewMap(FacesContext context) {
		Map<String, Object> viewMap = context.getViewRoot().getViewMap();
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Map<String, Object> idMap = (Map) viewMap.get(key);
		if (idMap == null) {
			idMap = new HashMap<String, Object>();
			viewMap.put(key, idMap);
		}
		return idMap;
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent c, String value) {
		try {
			if (value.isEmpty()) {
				return null;
			}
			return getViewMap(context).get(value);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent c, Object value) {
		try {
			if (value == null) {
				return empty;
			}
			String id = String.format("%s_%s", c.getClientId(),((BaseEntity) value).getId().toString());
			getViewMap(context).put(id, value);
			return id;
		} catch (Exception e) {
			return null;
		}
	}
}
