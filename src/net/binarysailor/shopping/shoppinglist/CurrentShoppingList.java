package net.binarysailor.shopping.shoppinglist;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import net.binarysailor.shopping.shoppinglist.model.ProductSelection;
import net.binarysailor.shopping.shoppinglist.model.ShoppingList;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class CurrentShoppingList implements Serializable, OnGroupExpandListener, OnGroupCollapseListener {

	private static final long serialVersionUID = 1L;

	private ProductSelection productSelection = new ProductSelection();
	private ShoppingList loadedList;
	private boolean listModified;
	private Set<Integer> expandedGroups = new HashSet<Integer>();
	private Set<Integer> doneProducts = new HashSet<Integer>();
	private boolean listening = true;

	private static CurrentShoppingList instance = new CurrentShoppingList();

	public static CurrentShoppingList getInstance() {
		return instance;
	}

	public boolean isListLoaded() {
		return loadedList != null;
	}

	public Integer getLoadedListId() {
		return loadedList != null ? loadedList.getId() : null;
	}

	public ProductSelection getProductSelection() {
		return productSelection;
	}

	public void selectProduct(int id) {
		productSelection.select(id);
	}

	public boolean isProductSelected(int id) {
		return productSelection.isSelected(id);
	}

	public void deselectProduct(int id) {
		productSelection.deselect(id);
		doneProducts.remove(id);
	}

	public boolean isGroupExpanded(int groupIndex) {
		return expandedGroups.contains(groupIndex);
	}

	public void loadList(ShoppingList list, boolean replace) {
		if (replace) {
			reset();
			this.loadedList = list;
		}
		productSelection.add(list.toSelection());
	}

	public void reset() {
		this.loadedList = null;
		productSelection.deselectAll();
		doneProducts.clear();
	}

	@Override
	public void onGroupCollapse(int groupPosition) {
		if (listening) {
			expandedGroups.remove(groupPosition);
		}
	}

	@Override
	public void onGroupExpand(int groupPosition) {
		if (listening) {
			expandedGroups.add(groupPosition);
		}
	}

	public void startListening() {
		listening = true;
	}

	public void stopListening() {
		listening = false;
	}

	public void clearDoneProducts() {
		doneProducts.clear();
	}

	public boolean isProductDone(int productId) {
		return doneProducts.contains(productId);
	}

	public void toggleProductDone(int productId) {
		if (doneProducts.contains(productId)) {
			doneProducts.remove(productId);
		} else {
			doneProducts.add(productId);
		}
	}

	public void deselectDoneProducts() {
		for (Integer productId : doneProducts) {
			productSelection.deselect(productId);
		}
	}

	public boolean isListModified() {
		return listModified;
	}

	public void setListModified(boolean modified) {
		this.listModified = modified;
	}
}
