package com.jgalante.balance.view;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import com.jgalante.balance.controller.ExportImportController;
import com.jgalante.balance.entity.Account;
import com.jgalante.balance.entity.AccountType;
import com.jgalante.balance.entity.Balance;
import com.jgalante.balance.entity.Category;
import com.jgalante.balance.entity.Person;
import com.jgalante.balance.entity.Transaction;
import com.jgalante.crud.entity.BaseEntity;
import com.jgalante.crud.util.Util;

@Named
@ViewScoped
public class ExportImportView implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ExportImportController exportImportController;

	private StreamedContent exportFile;

	private UploadedFile importFile;

	private String[] tables = { Person.class.getSimpleName(),
			Account.class.getSimpleName(), Category.class.getSimpleName(),
			Transaction.class.getSimpleName() };

	private String exportTable = Person.class.getSimpleName();

	private String importTable = Person.class.getSimpleName();

	public void exportData() {
		StringWriter json = new StringWriter();
		json.append("{");
		
		List<Person> people = exportImportController.findAllPeople();
		writeJson(Person.class, json, people);
		json.append(",");
		
		List<AccountType> accountsType = exportImportController.findAllAccountsType();
		writeJson(AccountType.class, json, accountsType);
		json.append(",");
		
		List<Account> accounts = exportImportController.findAllAccounts();
		writeJson(Account.class, json, accounts);
		json.append(",");
		
		List<Category> categories = exportImportController.findAllCategories();
		writeJson(Category.class, json, categories);
		json.append(",");
		
		List<Transaction> transactions = exportImportController
				.findAllTransactions();
		writeJson(Transaction.class, json, transactions);
		json.append(",");
		
		List<Balance> balances = exportImportController.findAllBalances();
		writeJson(Balance.class, json, balances);

//		List<Person> people = exportImportController.findAllPeople();
//		json.append(String.format("\"%s\":[", Person.class.getSimpleName()));
//		boolean first = true;
//		for (Person person : people) {
//			if (!first) {
//				json.append(",");
//			}
//			json.append(Util.writeJson(Person.class, person));
//			first = false;
//		}
//		json.append("],");
//		
//		List<AccountType> accountsType = exportImportController.findAllAccountsType();
//		json.append(String.format("\"%s\":[", Account.class.getSimpleName()));
//		first = true;
//		for (AccountType accountType : accountsType) {
//			if (!first) {
//				json.append(",");
//			}
//			json.append(Util.writeJson(AccountType.class, accountType));
//			first = false;
//		}
//		json.append("],");
//
//		List<Account> accounts = exportImportController.findAllAccounts();
//		json.append(String.format("\"%s\":[", Account.class.getSimpleName()));
//		first = true;
//		for (Account account : accounts) {
//			if (!first) {
//				json.append(",");
//			}
//			json.append(Util.writeJson(Account.class, account));
//			first = false;
//		}
//		json.append("],");
//
//		List<Category> categories = exportImportController.findAllCategories();
//		json.append(String.format("\"%s\":[", Category.class.getSimpleName()));
//		first = true;
//		for (Category category : categories) {
//			if (!first) {
//				json.append(",");
//			}
//			json.append(Util.writeJson(Category.class, category));
//			first = false;
//		}
//		json.append("],");
//
//		List<Transaction> transactions = exportImportController
//				.findAllTransactions();
//		json.append(String.format("\"%s\":[", Transaction.class.getSimpleName()));
//		first = true;
//		for (Transaction transaction : transactions) {
//			if (!first) {
//				json.append(",");
//			}
//			json.append(Util.writeJson(Transaction.class, transaction));
//			first = false;
//		}
//		json.append("],");
//
//		List<Balance> balances = exportImportController.findAllBalances();
//		json.append(String.format("\"%s\":[", Balance.class.getSimpleName()));
//		first = true;
//		for (Balance balance : balances) {
//			if (!first) {
//				json.append(",");
//			}
//			json.append(Util.writeJson(Balance.class, balance));
//			first = false;
//		}
//		json.append("]");

		json.append("}");

		InputStream is = new ByteArrayInputStream(json.toString().getBytes());
		exportFile = new DefaultStreamedContent(is, "text/json",
				"database.json");
	}
	
	protected <T extends BaseEntity> void writeJson(Class<T> entityClass, StringWriter json, List<T> list) {
		json.append(String.format("\"%s\":[", entityClass.getSimpleName()));
		boolean first = true;
		for (T item : list) {
			if (!first) {
				json.append(",");
			}
			json.append(Util.writeJson(entityClass, item));
			first = false;
		}
		json.append("]");
	}

	public void importData() {
		if (importFile != null) {
			// create JsonReader object
			JsonReader jsonReader;
			try {
				jsonReader = Json.createReader(importFile.getInputstream());

				// get JsonObject from JsonReader
				JsonObject jsonObject = jsonReader.readObject();

				// we can close IO resource and JsonReader now
				jsonReader.close();
				importFile.getInputstream().close();

				JsonArray jsonArray = jsonObject.getJsonArray(Person.class
						.getSimpleName());
				List<Person> people = new LinkedList<Person>();
				int size = 0;
				if (jsonArray != null) {
					size = jsonArray.size();
					for (int i = 0; i < size; i++) {
						people.add(Util.readJsonObject(Person.class,
								jsonArray.getJsonObject(i)));
					}
				}
				
				List<AccountType> accountsType = new LinkedList<AccountType>();
				readJsonObject(AccountType.class, jsonObject, accountsType);

				List<Account> accounts = new LinkedList<Account>();
				readJsonObject(Account.class, jsonObject, accounts);
				
				List<Category> categories = new LinkedList<Category>();
				readJsonObject(Category.class, jsonObject, categories);
				
				List<Transaction> transactions = new LinkedList<Transaction>();
				readJsonObject(Transaction.class, jsonObject, transactions);
				
				List<Balance> balances = new LinkedList<Balance>();
				readJsonObject(Balance.class, jsonObject, balances);

//				jsonArray = jsonObject.getJsonArray(Category.class
//						.getSimpleName());
//				List<Category> categories = new LinkedList<Category>();
//				if (jsonArray != null) {
//					size = jsonArray.size();
//					for (int i = 0; i < size; i++) {
//						categories.add(Util.readJsonObject(Category.class,
//								jsonArray.getJsonObject(i)));
//					}
//				}
//
//				jsonArray = jsonObject.getJsonArray(Transaction.class
//						.getSimpleName());
//				List<Transaction> transactions = new LinkedList<Transaction>();
//				if (jsonArray != null) {
//					size = jsonArray.size();
//					for (int i = 0; i < size; i++) {
//						transactions.add(Util.readJsonObject(Transaction.class,
//								jsonArray.getJsonObject(i)));
//					}
//				}
//
//				jsonArray = jsonObject.getJsonArray(Balance.class
//						.getSimpleName());
//				List<Balance> balances = new LinkedList<Balance>();
//				if (jsonArray != null) {
//					size = jsonArray.size();
//					for (int i = 0; i < size; i++) {
//						balances.add(Util.readJsonObject(Balance.class,
//								jsonArray.getJsonObject(i)));
//					}
//				}

				exportImportController.save(people, accountsType, accounts, categories,
						transactions, balances);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected <T extends BaseEntity> void readJsonObject(Class<T> entityClass, JsonObject jsonObject, List<T> list) {
		JsonArray jsonArray;
		int size;
		jsonArray = jsonObject.getJsonArray(entityClass.getSimpleName());
		if (jsonArray != null) {
			size = jsonArray.size();
			for (int i = 0; i < size; i++) {
				list.add(Util.readJsonObject(entityClass,
						jsonArray.getJsonObject(i)));
			}
		}
	}

	public StreamedContent getExportFile() {
		return exportFile;
	}

	public void setExportFile(StreamedContent exportFile) {
		this.exportFile = exportFile;
	}

	public UploadedFile getImportFile() {
		return importFile;
	}

	public void setImportFile(UploadedFile importFile) {
		this.importFile = importFile;
	}

	public String[] getTables() {
		return tables;
	}

	public String getImportTable() {
		return importTable;
	}

	public void setImportTable(String importTable) {
		this.importTable = importTable;
	}

	public String getExportTable() {
		return exportTable;
	}

	public void setExportTable(String exportTable) {
		this.exportTable = exportTable;
	}

}
