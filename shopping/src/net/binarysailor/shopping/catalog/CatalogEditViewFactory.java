package net.binarysailor.shopping.catalog;

import net.binarysailor.shopping.R;
import net.binarysailor.shopping.catalog.CatalogViewAdapter.CatalogViewFactory;
import net.binarysailor.shopping.catalog.model.Category;
import net.binarysailor.shopping.catalog.model.Product;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CatalogEditViewFactory implements CatalogViewFactory {

	@Override
	public View getProductView(Product product, Context context, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.catalog_edit_exp_list_product, parent, false);
		TextView tv = (TextView) view.findViewById(R.id.product_name);
		tv.setText(product.getName());

		View button = view.findViewById(R.id.product_edit_btn);
		button.setTag(product);
		button = view.findViewById(R.id.product_delete_btn);
		button.setTag(product);

		return view;
	}

	@Override
	public View getCategoryView(Category category, Context context, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.catalog_edit_exp_list_category, parent, false);
		TextView tv = (TextView) view.findViewById(R.id.category_name);
		tv.setText(category.getName());
		View editButton = view.findViewById(R.id.category_edit_btn);
		editButton.setFocusable(false);
		editButton.setTag(category);
		View deleteButton = view.findViewById(R.id.category_delete_btn);
		deleteButton.setFocusable(false);
		deleteButton.setTag(category);
		View addProductButton = view.findViewById(R.id.add_product_button);
		addProductButton.setFocusable(false);
		addProductButton.setTag(category);
		return view;
	}

}
