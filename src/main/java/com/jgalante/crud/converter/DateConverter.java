package com.jgalante.crud.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;


@FacesConverter("dateConverter")
public class DateConverter implements Converter {

	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if(value != null && value.trim().length() > 0) {
//            ThemeService service = (ThemeService) fc.getExternalContext().getApplicationMap().get("themeService");
//            return service.getThemes().get(Integer.parseInt(value));
        	return null;
        }
        else {
            return null;
        }
    }
 
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if(object != null) {
//            return String.valueOf(((Theme) object).getId());
        	return null;
        }
        else {
            return null;
        }
    }   
}
