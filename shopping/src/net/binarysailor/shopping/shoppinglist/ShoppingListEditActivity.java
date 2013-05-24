package net.binarysailor.shopping.shoppinglist;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.binarysailor.shopping.R;
import net.binarysailor.shopping.catalog.CatalogViewAdapter;
import net.binarysailor.shopping.catalog.dao.CatalogDAO;
import net.binarysailor.shopping.catalog.model.Category;
import net.binarysailor.shopping.shoppinglist.model.ProductSelection;
import net.binarysailor.shopping.shoppinglist.model.ProductSelectionFactory;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;

public class ShoppingListEditActivity extends Activity {

	ProductSelection productSelection;
	Set<Integer> expandedGroups = new HashSet<Integer>();
	TreeGroupExpandListener expandListener = new TreeGroupExpandListener(expandedGroups);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping);
		productSelection = ProductSelectionFactory.getProductSelection(getIntent());

		List<Category> categories = new CatalogDAO(this).getCategories();
		drawProductTree(categories);

		EditText searchField = (EditText) findViewById(R.id.searchText);
		searchField.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				ExpandableListView categorizedProducts = (ExpandableListView) findViewById(R.id.productTree);
				CatalogViewAdapter adapter = (CatalogViewAdapter) categorizedProducts.getExpandableListAdapter();
				String text = s.toString();
				adapter.setFilterText(text);
				if (text.trim().equals("")) {
					restoreExpandCollapseState();
				} else {
					expandAll();
				}
			}

			private void expandAll() {
				ExpandableListView categorizedProducts = (ExpandableListView) findViewById(R.id.productTree);
				CatalogViewAdapter adapter = (CatalogViewAdapter) categorizedProducts.getExpandableListAdapter();
				int groupCount = adapter.getGroupCount();
				expandListener.stopListening();
				for (int i = 0; i < groupCount; i++) {
					categorizedProducts.expandGroup(i);
				}
				expandListener.startListening();
			}
		});
	}

	public void clearSearchFilterClicked(View v) {
		EditText searchText = (EditText) findViewById(R.id.searchText);
		searchText.setText("");
	}

	public void productCheckboxClicked(View v) {
		if (v instanceof CheckBox) {
			CheckBox cb = (CheckBox) v;
			if (cb.isChecked()) {
				productSelection.select(cb.getId());
			} else {
				productSelection.deselect(cb.getId());
			}
		}
	}

	public void productNameClicked(View v) {
		CheckBox checkBox = (CheckBox) (((ViewGroup) v.getParent()).getChildAt(0));
		checkBox.toggle();
		productCheckboxClicked(checkBox);
	}

	private void drawProductTree(List<Category> categories) {
		ExpandableListView categorizedProducts = (ExpandableListView) findViewById(R.id.productTree);
		ShoppingListEditProductViewFactory productViewFactory = new ShoppingListEditProductViewFactory(
				this.productSelection);
		CatalogViewAdapter adapter = new CatalogViewAdapter(this, productViewFactory);
		categorizedProducts.setAdapter(adapter);
		categorizedProducts.setOnGroupExpandListener(expandListener);
		categorizedProducts.setOnGroupCollapseListener(expandListener);
	}

	public void showFlatView(View source) {
		Intent intent = new Intent(this, FlatShoppingListActivity.class);
		intent.putExtra("selection", this.productSelection);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@Override
	protected void onStart() {
		super.onStart();
		restoreExpandCollapseState();
	}

	private void restoreExpandCollapseState() {
		expandListener.stopListening();
		ExpandableListView categorizedProducts = (ExpandableListView) findViewById(R.id.productTree);
		int size = categorizedProducts.getExpandableListAdapter().getGroupCount();
		for (int i = 0; i < size; i++) {
			if (expandedGroups.contains(i)) {
				categorizedProducts.expandGroup(i);
			} else {
				categorizedProducts.collapseGroup(i);
			}
		}
		expandListener.startListening();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putIntegerArrayList("expandedGroups", new ArrayList<Integer>(expandedGroups));
		outState.putSerializable("productSelection", productSelection);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		ArrayList<Integer> savedExpandedIndices = savedInstanceState.getIntegerArrayList("expandedGroups");
		expandedGroups = new HashSet<Integer>(savedExpandedIndices);
		expandListener.setExpanded(expandedGroups);
		productSelection = (ProductSelection) savedInstanceState.getSerializable("productSelection");
	}
}
