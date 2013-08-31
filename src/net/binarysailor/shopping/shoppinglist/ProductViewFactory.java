package net.binarysailor.shopping.shoppinglist;

import net.binarysailor.shopping.R;
import net.binarysailor.shopping.catalog.model.Product;
import net.binarysailor.shopping.shoppinglist.model.ProductSelection;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

class ShoppingListEditProductViewFactory {

	private ProductSelection productSelection;

	public ShoppingListEditProductViewFactory(ProductSelection productSelection) {
		this.productSelection = productSelection;
	}

	public View getProductView(Product product, Context context, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.catalog_exp_list_child, parent, false);
		int productId = product.getId();
		TextView productNameLabel = (TextView) view.findViewById(R.id.product_name);
		productNameLabel.setText(product.getName());
		productNameLabel.setTextSize(10f);
		CheckBox checkbox = (CheckBox) view.findViewById(R.id.product_checkbox);
		checkbox.setId(productId);
		checkbox.setTag(product);
		checkbox.setChecked(product.getId() == 0 || productSelection.isSelected(productId));
		return view;
	}

	public View getProductGroupView(ProductGroup pg, Context context, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(android.R.layout.simple_expandable_list_item_1, parent, false);
		view.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
		ViewGroup.LayoutParams lp = new ListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 55);
		view.setLayoutParams(lp);
		TextView tv = (TextView) view.findViewById(android.R.id.text1);
		tv.setText(pg.getName());
		tv.setTextColor(context.getResources().getColor(android.R.color.white));
		tv.setTextSize(12f);
		return view;
	}

}
