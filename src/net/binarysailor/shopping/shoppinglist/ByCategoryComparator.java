package net.binarysailor.shopping.shoppinglist;

import java.util.Comparator;

import net.binarysailor.shopping.catalog.model.Product;

public class ByCategoryComparator implements Comparator<Product> {

	private static ByCategoryComparator instance = new ByCategoryComparator();

	@Override
	public int compare(Product lhs, Product rhs) {
		if (lhs.getCategory() != null && rhs.getCategory() != null) {
			return lhs.getCategory().compareTo(rhs.getCategory());
		}
		if (lhs.getCategory() == null && rhs.getCategory() != null) {
			return 1;
		}
		if (lhs.getCategory() != null && rhs.getCategory() == null) {
			return -1;
		}
		return 0;
	}

	public static Comparator<? super Product> getInstance() {
		return instance;
	}

}
