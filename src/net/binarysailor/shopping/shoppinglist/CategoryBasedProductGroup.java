package net.binarysailor.shopping.shoppinglist;

import net.binarysailor.shopping.catalog.model.Category;
import net.binarysailor.shopping.catalog.model.Product;

public class CategoryBasedProductGroup implements ProductGroup {
	private Category category;

	CategoryBasedProductGroup(Category category) {
		this.category = category;
	}

	@Override
	public Iterable<Product> getProducts() {
		return category.getProducts();
	}

	@Override
	public long getViewId() {
		return category.getId();
	}

	@Override
	public String getName() {
		return category.getName();
	}

}
