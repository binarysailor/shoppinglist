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

	public List<EnlistedProduct> getNonCatalogProducts() {
		List<EnlistedProduct> result = new LinkedList<EnlistedProduct>();
		for (EnlistedProduct ep : products) {
			if (!ep.isInCatalog()) {
				result.add(ep);
			}
		}
		return result;
	}

	public void enlistProduct(Product p, BigDecimal quantity) {
		EnlistedProduct ep = new EnlistedProduct(p, quantity);
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
		this.products.clear();
		for (Product p : products) {
			enlistProduct(p, BigDecimal.ONE);
		}
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
