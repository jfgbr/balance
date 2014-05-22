package com.jgalante.balance.persistence;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;


@ApplicationScoped
//@Startup
public class Initializer {
	
	@Inject
	private TransactionDAO personDAO;
	
    @PostConstruct
    public void populate() {
//    	crudDAO.setEntityClass(Person.class);
//    	Person person = new Person("Jonas");
//    	personDAO.save(person);
//    	person = new Person("Marina");
//    	personDAO.save(person);
    }
}
