package net.binarysailor.shopping.shoppinglist.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import net.binarysailor.shopping.catalog.model.Product;

public class ShoppingList implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
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

	public void enlistProduct(Product p, BigDecimal quantity) {
		EnlistedProduct ep = new EnlistedProduct(p, quantity);
		products.add(ep);
	}

	public void enlistNonCatalogProduct(String name, BigDecimal quantity) {
		EnlistedProduct ep = new EnlistedProduct(name, quantity);
		products.add(ep);
	}

	public ProductSelection toSelection() {
		ProductSelection selection = new ProductSelection();
		for (EnlistedProduct ep : products) {
			selection.select(ep.getProduct().getId());
		}
		return selection;
	}

	public void setProducts(Collection<Product> products) {
		products.clear();
		products.addAll(products);
	}

	@Override
	public Object clone() {
		ShoppingList c;
		try {
			c = (ShoppingList) super.clone();
			c.products = new LinkedList<EnlistedProduct>();
			for (EnlistedProduct ep : this.products) {
				c.products.add((EnlistedProduct) ep.clone());
			}
			return c;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

}
