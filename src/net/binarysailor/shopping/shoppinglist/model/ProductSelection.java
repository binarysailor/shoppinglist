package net.binarysailor.shopping.shoppinglist.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ProductSelection implements Serializable {
	private static final long serialVersionUID = 1L;

	private Set<Integer> selectedIds = new HashSet<Integer>();

	public boolean isSelected(int productId) {
		return selectedIds.contains(productId);
	}

	public void select(int productId) {
		selectedIds.add(productId);
	}

	public void deselect(int productId) {
		selectedIds.remove(productId);
	}

	public void add(ProductSelection other) {
		selectedIds.addAll(other.selectedIds);
	}

	public void deselectAll() {
		selectedIds.clear();
	}

	public Collection<Integer> getProductIds() {
		return Collections.unmodifiableSet(selectedIds);
	}
}
