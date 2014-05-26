package com.jgalante.balance.view;

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
import com.jgalante.balance.entity.Transaction;

@Named
@ViewScoped
public class TransactionView extends TesteView<Transaction, TransactionController> {

	private static final long serialVersionUID = 1L;

	private List<Category> categories;
	private TreeNode root;
	
	@PostConstruct
	public void init() {
		setEntity(new Transaction());
//		setEntities(getController().findAll());
		
//		categories = ((TransactionController)getController()).findCategories();
		
//		List<Category> parents = ((TransactionController)getController()).findCategoriesByParent(null);
//		root = new DefaultTreeNode(new Group(new Category("Balance", new LinkedHashSet<Category>(parents)), null), null);
		root = new DefaultTreeNode(new Group(new Category("Balance"), new Transaction()), null);
		
		createTreeCategories(root, null);
		
		TreeNode node = new DefaultTreeNode(new Group(new Category("Total"), ((Group)root.getData()).getTransaction()), root);
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

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public TreeNode getRoot() {
        return root;
    }
}
