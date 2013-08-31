package net.binarysailor.shopping.catalog.model;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Category implements Serializable, Comparable<Category> {
	private static final long serialVersionUID = 1L;

	int id;
	String name;
	String order;
	List<Product> products = new LinkedList<Product>();

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

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void addProduct(Product p) {
		products.add(p);
		p.setCategory(this);
	}

	public void removeProduct(Product product) {
		for (Iterator<Product> i = products.iterator(); i.hasNext();) {
			Product p = i.next();
			if (p == product) {
				i.remove();
				p.setCategory(null);
			}
		}
	}

	public boolean isNew() {
		return id == 0;
	}

	public void removeAllProducts() {
		for (Product p : products) {
			p.setCategory(null);
		}
		products.clear();
	}

	@Override
	public int compareTo(Category another) {
		return order.compareTo(another.order);
	}
}
