package com.jgalante.balance.view;

import java.util.List;

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
		
		categories = ((TransactionController)getController()).findCategories();
		
		root = new DefaultTreeNode(new Group(new Category("Balance", null), null), null);
		
		createTreeCategories(root, null);
	}
	
	public void createTreeCategories(TreeNode root, Long idParent) {
//		Category parent = ((Category)root.getData()).getParent();
//		Long idParent = null;
//		if (parent != null) {
//			idParent = parent.getId();
//		}
		List<Category> parents = ((TransactionController)getController()).findCategoriesByParent(idParent);
        for (Category category : parents) {
        	TreeNode node = new DefaultTreeNode(new Group(category, category.getTransactions()), root);
        	if (category.getSubCategories() != null && !category.getSubCategories().isEmpty()) {
        		createTreeCategories(node, category.getId());
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
