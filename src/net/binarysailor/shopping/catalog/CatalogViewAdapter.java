package net.binarysailor.shopping.catalog;

import java.util.ArrayList;
import java.util.List;

import net.binarysailor.shopping.catalog.dao.CatalogDAO;
import net.binarysailor.shopping.catalog.model.Category;
import net.binarysailor.shopping.catalog.model.Product;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

public class CatalogViewAdapter extends BaseExpandableListAdapter {

	public interface CatalogViewFactory {
		View getProductView(Product product, Context context, ViewGroup parent);

		View getCategoryView(Category category, Context context, ViewGroup parent);
	}

	Context context;
	List<Category> categories;
	List<FilteredCategory> filteredCategories;
	CatalogViewFactory productViewFactory;
	String filterText;
	Category nonCatalogItems;

	public CatalogViewAdapter(Context context, CatalogViewFactory productViewFactory) {
		this.context = context;
		this.categories = new CatalogDAO(context).getCategories();
		this.productViewFactory = productViewFactory;
		this.filteredCategories = new ArrayList<FilteredCategory>(categories.size());
		this.nonCatalogItems = new Category();
		this.nonCatalogItems.setName("Extra");
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
		return filteredCategories.get(groupPosition).getProducts().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return filteredCategories.get(groupPosition).getCategory();
	}

	@Override
	public int getGroupCount() {
		return filteredCategories.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return filteredCategories.get(groupPosition).getCategory().getId();
	}

	@Override
	public View getGroupView(int groupPosition, boolean expanded, View cv, ViewGroup parent) {
		Category category = filteredCategories.get(groupPosition).getCategory();
		return productViewFactory.getCategoryView(category, context, parent);
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
		FilteredCategory category = filteredCategories.get(groupPosition);
		return category.getProducts().get(childPosition);
	}

	private void filterCategories() {
		filteredCategories.clear();
		for (Category category : categories) {
			FilteredCategory filteredCategory = FilteredCategory.create(category, filterText);
			if (filteredCategory != null) {
				filteredCategories.add(filteredCategory);
			}
		}
		FilteredCategory filteredNonCatalog = FilteredCategory.create(nonCatalogItems, filterText);
		if (filteredNonCatalog != null && !filteredNonCatalog.isEmpty()) {
			filteredCategories.add(filteredNonCatalog);
		}
	}

	public void addNonCatalogProduct(String name) {
		Product pseudoProduct = new Product();
		pseudoProduct.setName(name);
		nonCatalogItems.addProduct(pseudoProduct);
		notifyDataSetChanged();
	}
}
