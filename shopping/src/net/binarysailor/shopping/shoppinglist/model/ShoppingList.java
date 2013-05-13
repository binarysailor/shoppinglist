package net.binarysailor.shopping.shoppinglist.model;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import net.binarysailor.shopping.catalog.model.Product;

public class ShoppingList {
	private int id;
	private String name;
	private List<EnlistedProduct> products = new LinkedList<EnlistedProduct>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<EnlistedProduct> getProducts() {
		return products;
	}

	public void setProducts(List<EnlistedProduct> products) {
		this.products = products;
	}

	public void enlistProduct(Product p, BigDecimal quantity) {
		EnlistedProduct ep = new EnlistedProduct(p, quantity);
		products.add(ep);
	}

}
