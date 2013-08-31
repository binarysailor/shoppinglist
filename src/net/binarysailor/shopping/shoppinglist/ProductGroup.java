package net.binarysailor.shopping.shoppinglist;

import net.binarysailor.shopping.catalog.model.Product;

public interface ProductGroup {
	Iterable<Product> getProducts();

	public long getViewId();

	String getName();
}
