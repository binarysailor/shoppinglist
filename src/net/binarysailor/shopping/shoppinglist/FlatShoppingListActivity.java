package net.binarysailor.shopping.shoppinglist;

import java.util.HashSet;
import java.util.Set;

import net.binarysailor.shopping.R;
import net.binarysailor.shopping.shoppinglist.model.ProductSelection;
import net.binarysailor.shopping.shoppinglist.model.ProductSelectionFactory;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FlatShoppingListActivity extends Activity {
	ProductSelection productSelection;
	Set<Integer> doneProducts = new HashSet<Integer>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flat_shopping);
		productSelection = ProductSelectionFactory.getProductSelection(getIntent());
		drawItems();
	}

	private void drawItems() {
		ListView list = (ListView) findViewById(R.id.flatShoppingList);
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

	public void productNameClicked(View v) {
		TextView tv = (TextView) v;
		if (doneProducts.contains(v.getId())) {
			tv.setTextColor(Color.BLACK);
			tv.setPaintFlags(tv.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
			doneProducts.remove(v.getId());
		} else {
			tv.setTextColor(Color.LTGRAY);
			tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			doneProducts.add(v.getId());
		}
	}
}
