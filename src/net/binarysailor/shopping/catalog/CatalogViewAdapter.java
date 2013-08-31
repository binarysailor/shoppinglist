package net.binarysailor.shopping.catalog;

import java.util.List;

import net.binarysailor.shopping.catalog.dao.CatalogDAO;
import net.binarysailor.shopping.catalog.model.Category;
import net.binarysailor.shopping.catalog.model.Product;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

class CatalogViewAdapter extends BaseExpandableListAdapter {

	Context context;
	List<Category> categories;
	CatalogEditViewFactory productViewFactory;
	String filterText;

	public CatalogViewAdapter(Context context) {
		this.context = context;
		categories = new CatalogDAO(context).getCategories();
		this.productViewFactory = new CatalogEditViewFactory();
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
		return getCategory(groupPosition).getProducts().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return getCategory(groupPosition);
	}

	private Category getCategory(int groupPosition) {
		return categories.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return categories.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return getCategory(groupPosition).getId();
	}

	@Override
	public View getGroupView(int groupPosition, boolean expanded, View cv, ViewGroup parent) {
		return productViewFactory.getCategoryView(getCategory(groupPosition), context, parent);
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	public void setFilterText(String filter) {
		this.filterText = filter;
		notifyDataSetChanged();
	}

	private Product getProduct(int groupPosition, int childPosition) {
		Category category = getCategory(groupPosition);
		return category.getProducts().get(childPosition);
	}

}
