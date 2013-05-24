package net.binarysailor.shopping.catalog;

import java.util.LinkedList;
import java.util.List;

import net.binarysailor.shopping.catalog.model.Category;
import net.binarysailor.shopping.catalog.model.Product;

class FilteredCategory {

	private Category category;
	private List<Product> products;
	private String filterText;

	public static FilteredCategory create(Category category, String filterText) {
		FilteredCategory fc = new FilteredCategory(category, filterText);
		for (Product product : category.getProducts()) {
			fc.addProductIfMatches(product);
		}
		return fc.filteringRequested() && fc.isEmpty() ? null : fc;
	}

	private FilteredCategory(Category category, String filterText) {
		this.category = category;
		this.products = new LinkedList<Product>();
		this.filterText = filterText;
		if (this.filterText != null) {
			this.filterText = this.filterText.toLowerCase();
		}
	}

	public Category getCategory() {
		return category;
	}

	public List<Product> getProducts() {
		return products;
	}

	public boolean isEmpty() {
		return products.isEmpty();
	}

	private boolean addProductIfMatches(Product product) {
		if (!filteringRequested() || matches(product)) {
			products.add(product);
			return true;
		}
		return false;
	}

	private boolean matches(Product product) {
		return product.getName().toLowerCase().contains(filterText);
	}

	private boolean filteringRequested() {
		return filterText != null && !filterText.trim().equals("");
	}
}
