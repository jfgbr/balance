package com.jgalante.crud.handler;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

public class MyExceptionHandlerFactory extends ExceptionHandlerFactory {

	private ExceptionHandlerFactory parent;

	// this injection handles jsf
	public MyExceptionHandlerFactory(ExceptionHandlerFactory parent) {
		this.parent = parent;
	}

	// create your own ExceptionHandlers
	@Override
	public ExceptionHandler getExceptionHandler() {
		ExceptionHandler result = new MyExceptionHandler(
				parent.getExceptionHandler());
		return result;
	}

}
