package com.jgalante.crud.handler;

import javax.faces.FacesException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;

public class MyExceptionHandler extends ExceptionHandlerWrapper {

	private ExceptionHandler wrapped;
	
	public MyExceptionHandler(ExceptionHandler wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public ExceptionHandler getWrapped() {
		return wrapped;
	}
	
	@Override
	public void handle() throws FacesException {
//		for (final Iterator<ExceptionQueuedEvent> it = getUnhandledExceptionQueuedEvents()
//				.iterator(); it.hasNext();) {
//			
//			ExceptionQueuedEvent event = it.next();
//            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
//			Throwable t = context.getException();
//			t.printStackTrace();
//            it.remove();
//		}
//		
//		for (final Iterator<ExceptionQueuedEvent> it = getHandledExceptionQueuedEvents()
//				.iterator(); it.hasNext();) {
//			
//			ExceptionQueuedEvent event = it.next();
//            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
//			Throwable t = context.getException();
//			t.printStackTrace();
//            it.remove();
//		}
		
		getWrapped().handle();
	}

}
