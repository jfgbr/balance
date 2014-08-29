package com.jgalante.balance.view;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.jgalante.balance.controller.CategoryController;
import com.jgalante.balance.entity.Category;
import com.jgalante.crud.util.Util;

@Named
@ViewScoped
public class ExportImportView implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject
	private CategoryController categoryController;
	
	private List<Category> categories;
	
	private StreamedContent file;

	public void exportCategories() {
		StringWriter json = new StringWriter();
		categories = categoryController.findAll();
		for (Category category : categories) {
			json.append(Util.writeJson(Category.class, category));
		}
		
		InputStream is = new ByteArrayInputStream(json.toString().getBytes());
		file = new DefaultStreamedContent(is, "text/csv", "category.csv");
//		setEntity(Util.readJson(Category.class, Util.writeJson(Transaction.class, base)));
	}

	public StreamedContent getFile() {
		return file;
	}
	
}
