package net.binarysailor.shopping.shoppinglist;

import java.util.Arrays;

import net.binarysailor.shopping.R;
import net.binarysailor.shopping.catalog.dao.CatalogDAO;
import net.binarysailor.shopping.catalog.model.Product;
import net.binarysailor.shopping.shoppinglist.model.ProductSelection;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

class FlatListAdapter extends ArrayAdapter<Product> {

	Product[] products;

	public FlatListAdapter(Context context, ProductSelection selection) {
		super(context, 0);
		products = new CatalogDAO(context).getProducts(selection).toArray(new Product[0]);
		Arrays.sort(products, ByCategoryComparator.getInstance());
		for (Product p : products) {
			add(p);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.activity_flat_list_child, parent, false);
		TextView tv = (TextView) view.findViewById(android.R.id.text1);
		Product product = products[position];
		tv.setId(product.getId());
		tv.setText(product.getName());
		boolean done = FlatShoppingListActivity.isProductDone(product.getId());
		int color = done ? Color.LTGRAY : Color.BLACK;
		int flags = done ? (tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG) : (tv.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
		tv.setTextColor(color);
		tv.setPaintFlags(flags);
		return view;
	}
}
