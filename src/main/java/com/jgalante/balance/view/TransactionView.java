package com.jgalante.balance.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import com.jgalante.balance.controller.TransactionController;
import com.jgalante.balance.entity.Category;
import com.jgalante.balance.entity.Group;
import com.jgalante.balance.entity.Group2;
import com.jgalante.balance.entity.Transaction;

@Named
@ViewScoped
public class TransactionView extends TesteView<Transaction, TransactionController> {

	private static final long serialVersionUID = 1L;

	private List<Category> categories;
	private TreeNode root;
	private List<Group2> group;
	private List<ColumnModel> dates;
	
	static public class ColumnModel implements Serializable {

		private static final long serialVersionUID = 1L;
		
		private String header;
		private String property;
		private BigDecimal value;
		
		public ColumnModel(String header) {
			super();
			this.header = header;
		}
		
		public ColumnModel(String header, BigDecimal value) {
			super();
			this.header = header;
			this.value = value;
		}
		
		public ColumnModel(String header, String property, BigDecimal value) {
			super();
			this.header = header;
			this.property = property;
			this.value = value;
		}

		public String getHeader() {
			return header;
		}

		public String getProperty() {
			return property;
		}

		public BigDecimal getValue() {
			return value;
		}

		public void setValue(BigDecimal value) {
			this.value = value;
		}

	}
	
	@PostConstruct
	public void init() {
		dates = new LinkedList<TransactionView.ColumnModel>();
		for (int i = 0; i < 12; i++) {
			dates.add(new ColumnModel(Integer.toString(i+1)));
		}
		setEntity(new Transaction());
//		setEntities(getController().findAll());
		
//		categories = ((TransactionController)getController()).findCategories();
		
//		List<Category> parents = ((TransactionController)getController()).findCategoriesByParent(null);
//		root = new DefaultTreeNode(new Group(new Category("Balance", new LinkedHashSet<Category>(parents)), null), null);
		root = new DefaultTreeNode(new Group(new Category("Balance"), new Transaction()), null);
		
//		createTreeCategories(root, null);
		
//		new DefaultTreeNode(new Group(new Category("Total"), ((Group)root.getData()).getTransaction()), root);
		
		createListCategories(null,null);
	}
	
	public void createTreeCategories(TreeNode root, Long idParent) {
//		Category parent = ((Category)root.getData()).getParent();
//		Long idParent = null;
//		if (parent != null) {
//			idParent = parent.getId();
//		}
		List<Category> categories = ((TransactionController)getController()).findCategoriesByParent(idParent);
//		Set<Category> parents = ((Group)root.getData()).getCategory().getSubCategories(); 
        for (Category category : categories) {
        	Set<Transaction> transactions = category.getTransactions();
        	TreeNode node = null;
        	if (transactions != null && !transactions.isEmpty()) {
        		for (Transaction transaction : transactions) {
        			((Group)root.getData()).getTransaction().addValue(transaction.getValue());
        			node = new DefaultTreeNode(new Group(category, transaction), root);
				}
        	} else {
        		node = new DefaultTreeNode(new Group(category, new Transaction()), root);
        	}
        	
        	if (category.getSubCategories() != null && !category.getSubCategories().isEmpty()) {
        		createTreeCategories(node, category.getId());
        		((Group)root.getData()).getTransaction().addValue(((Group)node.getData()).getTransaction().getValue());
        	}
		}
	}

	public void createListCategories(List<Group2> root, Long idParent) {
//		List<Category> categories = ((TransactionController)getController()).findCategoriesByParent(idParent);
//		for (Category category : categories) {
////			root.add(new Group2(category, null, null));
//			Group2 node = null;
//			if (category.getSubCategories() != null && !category.getSubCategories().isEmpty()) {
//				createListCategories(root, category.getId());
//			}
//		}
		group = ((TransactionController)getController()).findGroupsByParent(idParent);
	}
	
	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public TreeNode getRoot() {
        return root;
    }

	public List<Group2> getGroup() {
		return group;
	}

	public List<ColumnModel> getDates() {
		return dates;
	}
	
}
