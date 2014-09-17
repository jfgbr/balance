package com.jgalante.crud.view;

import java.io.Serializable;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.jgalante.crud.util.Util;

public class SimpleView implements Serializable {

	private static final long serialVersionUID = 1L;
	
	protected String getMessage(String key){
		try{
			return ResourceBundle.getBundle("MessageResources")
					.getString(Util.getClass(this.getClass(), 0).getSimpleName().toLowerCase() + "." + key);
		}catch(MissingResourceException | ClassCastException e){
			return ResourceBundle.getBundle("MessageResources")
					.getString("default." + key);
		}
	}

}
