package net.binarysailor.shopping.shoppinglist;

import java.util.ArrayList;
import java.util.List;

import net.binarysailor.shopping.catalog.dao.CatalogDAO;
import net.binarysailor.shopping.catalog.model.Category;
import net.binarysailor.shopping.catalog.model.Product;
import net.binarysailor.shopping.shoppinglist.model.ProductSelection;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

class CatalogViewAdapter extends BaseExpandableListAdapter {

	Context context;
	List<ProductGroup> productGroups;
	NonCatalogProductGroup nonCatalogProducts;
	List<FilteredProductGroup> filteredProductGroups;
	ShoppingListEditProductViewFactory productViewFactory;
	String filterText;

	public CatalogViewAdapter(Context context, ProductSelection productSelection) {
		this.context = context;
		List<Category> categories = new CatalogDAO(context).getCategories();
		productGroups = new ArrayList<ProductGroup>(categories.size() * 2);
		for (Category category : categories) {
			productGroups.add(new CategoryBasedProductGroup(category));
		}
		nonCatalogProducts = new NonCatalogProductGroup();
		this.productViewFactory = new ShoppingListEditProductViewFactory(productSelection);
		this.filteredProductGroups = new ArrayList<FilteredProductGroup>(productGroups.size());
		filterCategories();
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		Product product = getProduct(groupPosition, childPosition);
		return product.getId();
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean lastChild, View cv, ViewGroup parent) {
		Product product = getProduct(groupPosition, childPosition);
		return productViewFactory.getProductView(product, context, parent);
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return filteredProductGroups.get(groupPosition).getProducts().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return getProductGroup(groupPosition);
	}

	private ProductGroup getProductGroup(int groupPosition) {
		return productGroups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return filteredProductGroups.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return getProductGroup(groupPosition).getViewId();
	}

	@Override
	public View getGroupView(int groupPosition, boolean expanded, View cv, ViewGroup parent) {
		ProductGroup productGroup = getProductGroup(groupPosition);
		return productViewFactory.getProductGroupView(productGroup, context, parent);
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	@Override
	public void notifyDataSetChanged() {
		filterCategories();
		super.notifyDataSetChanged();
	}

	public void setFilterText(String filter) {
		this.filterText = filter;
		notifyDataSetChanged();
	}

	private Product getProduct(int groupPosition, int childPosition) {
		FilteredProductGroup pg = filteredProductGroups.get(groupPosition);
		return pg.getProducts().get(childPosition);
	}

	private void filterCategories() {
		filteredProductGroups.clear();
		for (ProductGroup pg : productGroups) {
			FilteredProductGroup filteredProductGroup = FilteredProductGroup.create(pg, filterText);
			if (filteredProductGroup != null) {
				filteredProductGroups.add(filteredProductGroup);
			}
		}
	}

	public void addNonCatalogProduct(Product product) {
		if (!nonCatalogProducts.contains(product)) {
			nonCatalogProducts.addProduct(product);
		}
		showNonCatalogProducts();
		notifyDataSetChanged();
	}

	public void removeNonCatalogProduct(Product product) {
		nonCatalogProducts.remove(product);
		if (nonCatalogProducts.isEmpty()) {
			hideNonCatalogProducts();
		}
		notifyDataSetChanged();
	}

	public void clearNonCatalogProducts() {
		nonCatalogProducts.clear();
		hideNonCatalogProducts();
	}

	private void showNonCatalogProducts() {
		if (!productGroups.contains(nonCatalogProducts)) {
			productGroups.add(nonCatalogProducts);
		}
	}

	private void hideNonCatalogProducts() {
		productGroups.remove(nonCatalogProducts);
	}
}
