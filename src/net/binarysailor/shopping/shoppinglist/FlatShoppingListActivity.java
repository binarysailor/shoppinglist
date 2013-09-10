package net.binarysailor.shopping.shoppinglist;

import java.util.HashSet;
import java.util.Set;

import net.binarysailor.shopping.R;
import net.binarysailor.shopping.catalog.model.Product;
import net.binarysailor.shopping.shoppinglist.model.ProductSelection;
import net.binarysailor.shopping.shoppinglist.model.ProductSelectionFactory;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class FlatShoppingListActivity extends Activity {
	ProductSelection productSelection;
	private static Set<Integer> doneProducts = new HashSet<Integer>();

	public static void clearDoneProducts() {
		doneProducts.clear();
	}

	public static boolean isProductDone(int productId) {
		return doneProducts.contains(productId);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flat_shopping);
		productSelection = ProductSelectionFactory.getProductSelection(getIntent());
		drawItems();

	}

	private void drawItems() {
		ListView list = findListView();
		ListAdapter productAdapter = new FlatListAdapter(this, productSelection);
		list.setAdapter(productAdapter);
	}

	public void showFlatView(View source) {
		Intent intent = new Intent(this, FlatShoppingListActivity.class);
		intent.putExtra("selection", this.productSelection);
		startActivity(intent);
	}

	public void showTreeView(View source) {
		Intent intent = new Intent(this, ShoppingListEditActivity.class);
		intent.putExtra("selection", this.productSelection);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
	}

	@SuppressWarnings("unchecked")
	public void productNameClicked(View v) {
		if (doneProducts.contains(v.getId())) {
			doneProducts.remove(v.getId());
		} else {
			doneProducts.add(v.getId());
		}
		((ArrayAdapter<Product>) findListView().getAdapter()).notifyDataSetChanged();

	}

	private ListView findListView() {
		return (ListView) findViewById(R.id.flatShoppingList);
	}
}
