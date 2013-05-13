package net.binarysailor.shopping.shoppinglist;

import java.util.LinkedList;
import java.util.List;

import net.binarysailor.shopping.R;
import net.binarysailor.shopping.catalog.model.Category;
import net.binarysailor.shopping.catalog.model.Product;
import net.binarysailor.shopping.shoppinglist.model.ProductSelection;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

class FlatListAdapter extends ArrayAdapter<Product> {

	List<Product> products;

	public FlatListAdapter(Context context, List<Category> categories, ProductSelection selection) {
		super(context, 0);
		products = new LinkedList<Product>();
		for (Category c : categories) {
			for (Product p : c.getProducts()) {
				if (selection.isSelected(p.getId())) {
					add(p);
					products.add(p);
				}
			}
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.activity_flat_list_child, parent, false);
		TextView tv = (TextView) view.findViewById(android.R.id.text1);
		tv.setId(products.get(position).getId());
		tv.setText(products.get(position).getName());
		return view;
	}
}
