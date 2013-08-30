package net.binarysailor.shopping.catalog;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

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
			this.filterText = this.filterText.toLowerCase(Locale.getDefault());
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
		String lc = product.getName().toLowerCase(Locale.getDefault());
		return lc.contains(filterText) || denationalize(lc).contains(filterText);
	}

	private String denationalize(String lc) {
		StringBuilder sb = new StringBuilder(lc);
		for (int i = 0; i < sb.length(); i++) {
			switch (sb.charAt(i)) {
			case 'ą':
				sb.setCharAt(i, 'a');
				break;
			case 'ć':
				sb.setCharAt(i, 'c');
				break;
			case 'ę':
				sb.setCharAt(i, 'e');
				break;
			case 'ł':
				sb.setCharAt(i, 'l');
				break;
			case 'ń':
				sb.setCharAt(i, 'n');
				break;
			case 'ó':
				sb.setCharAt(i, 'o');
				break;
			case 'ś':
				sb.setCharAt(i, 's');
				break;
			case 'ż':
			case 'ź':
				sb.setCharAt(i, 'z');
				break;
			}
		}
		return sb.toString();
	}

	private boolean filteringRequested() {
		return filterText != null && !filterText.trim().equals("");
	}
}
