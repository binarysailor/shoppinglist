package net.binarysailor.shopping.shoppinglist;

import net.binarysailor.shopping.R;
import net.binarysailor.shopping.catalog.CatalogViewAdapter.CatalogViewFactory;
import net.binarysailor.shopping.catalog.model.Category;
import net.binarysailor.shopping.catalog.model.Product;
import net.binarysailor.shopping.shoppinglist.model.ProductSelection;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

class ShoppingListEditProductViewFactory implements CatalogViewFactory {

	private ProductSelection productSelection;

	public ShoppingListEditProductViewFactory(ProductSelection productSelection) {
		this.productSelection = productSelection;
	}

	@Override
	public View getProductView(Product product, Context context, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.catalog_exp_list_child, parent, false);
		int productId = product.getId();
		TextView productNameLabel = (TextView) view.findViewById(R.id.product_name);
		productNameLabel.setText(product.getName());
		CheckBox checkbox = (CheckBox) view.findViewById(R.id.product_checkbox);
		checkbox.setId(productId);
		checkbox.setChecked(productSelection.isSelected(productId));
		return view;
	}

	@Override
	public View getCategoryView(Category category, Context context, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(android.R.layout.simple_expandable_list_item_1, parent, false);
		view.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
		TextView tv = (TextView) view.findViewById(android.R.id.text1);
		tv.setText(category.getName());
		tv.setTextColor(context.getResources().getColor(android.R.color.white));
		return view;
	}

}
