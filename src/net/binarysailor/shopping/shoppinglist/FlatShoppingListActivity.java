package net.binarysailor.shopping.shoppinglist;

import net.binarysailor.shopping.R;
import net.binarysailor.shopping.catalog.model.Product;
import net.binarysailor.shopping.shoppinglist.dao.ShoppingListDAO;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class FlatShoppingListActivity extends Activity {
	private CurrentShoppingList currentList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flat_shopping);
		currentList = CurrentShoppingList.getInstance();
		drawItems();

	}

	private void drawItems() {
		ListView list = findListView();
		ListAdapter productAdapter = new FlatListAdapter(this, currentList);
		list.setAdapter(productAdapter);
	}

	public void showFlatView(View source) {
		Intent intent = new Intent(this, FlatShoppingListActivity.class);
		intent.putExtra("selection", currentList.getProductSelection());
		startActivity(intent);
	}

	public void showTreeView(View source) {
		Intent intent = new Intent(this, ShoppingListEditActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
	}

	@SuppressWarnings("unchecked")
	public void productNameClicked(View v) {
		currentList.toggleProductDone(v.getId());
		((ArrayAdapter<Product>) findListView().getAdapter()).notifyDataSetChanged();
	}

	public void finishShopping(View v) {
		currentList.deselectDoneProducts();
		if (currentList.isListLoaded()) {
			new ShoppingListDAO(this).updateShoppingListContents(currentList.getLoadedListId(), currentList.getProductSelection());
			currentList.setListModified(false);
		} else {
			currentList.setListModified(true);
		}

		Intent intent = new Intent(this, ShoppingListEditActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
	}

	private ListView findListView() {
		return (ListView) findViewById(R.id.flatShoppingList);
	}
}
