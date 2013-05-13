package net.binarysailor.shopping.catalog;

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
	CatalogViewFactory productViewFactory;

	public CatalogViewAdapter(Context context, CatalogViewFactory productViewFactory) {
		this.context = context;
		this.categories = new CatalogDAO(context).getCategories();
		this.productViewFactory = productViewFactory;
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
		return categories.get(groupPosition).getProducts().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return categories.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return categories.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return categories.get(groupPosition).getId();
	}

	@Override
	public View getGroupView(int groupPosition, boolean expanded, View cv, ViewGroup parent) {
		Category category = categories.get(groupPosition);
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

	private Product getProduct(int groupPosition, int childPosition) {
		Category category = categories.get(groupPosition);
		return category.getProducts().get(childPosition);
	}
}
