package net.binarysailor.shopping.shoppinglist;

import java.util.Collection;
import java.util.LinkedList;

import net.binarysailor.shopping.catalog.model.Product;

public class NonCatalogProductGroup implements ProductGroup {
	private Collection<Product> products = new LinkedList<Product>();
	private String name = "Extra";

	@Override
	public Iterable<Product> getProducts() {
		return products;
	}

	@Override
	public long getViewId() {
		return 0;
	}

	@Override
	public String getName() {
		return name;
	}

	public void addProduct(Product product) {
		products.add(product);
	}

	public void remove(Product product) {
		products.remove(product);
	}

	public void clear() {
		products.clear();
	}

	public boolean isEmpty() {
		return products.isEmpty();
	}

	public boolean contains(Product product) {
		return products.contains(product);
	}
}
