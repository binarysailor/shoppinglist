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
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ExpandableListView;

public class ShoppingListEditActivity extends Activity {

	ProductSelection productSelection;
	Set<Integer> expandedGroups = new HashSet<Integer>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping);
		productSelection = ProductSelectionFactory.getProductSelection(getIntent());

		List<Category> categories = new CatalogDAO(this).getCategories();
		drawProductTree(categories);
	}

	public void productTouched(View v) {
		v.setBackgroundColor(Color.BLUE);
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
		// categorizedProducts.setAdapter(this, new
		// ShoppingAdapter(categories));
		/*
		List<Map<String, String>> categoryMaps = new LinkedList<Map<String, String>>();
		List<List<Map<String, String>>> productMaps = new LinkedList<List<Map<String, String>>>();
		for (Category c : categories) {
			Map<String, String> cm = new HashMap<String, String>();
			cm.put("name", c.getName());
			categoryMaps.add(cm);
			List<Map<String, String>> productsForCategory = new LinkedList<Map<String, String>>();
			for (Product p : c.getProducts()) {
				Map<String, String> pm = new HashMap<String, String>();
				pm.put("name", p.getName());
				productsForCategory.add(pm);
			}
			productMaps.add(productsForCategory);
		}
		*/
		ShoppingListEditProductViewFactory productViewFactory = new ShoppingListEditProductViewFactory(
				this.productSelection);
		categorizedProducts.setAdapter(new CatalogViewAdapter(this, productViewFactory));
		TreeGroupExpandListener listener = new TreeGroupExpandListener(expandedGroups);

		categorizedProducts.setOnGroupExpandListener(listener);
		categorizedProducts.setOnGroupCollapseListener(listener);
	}

	public void showFlatView(View source) {
		Intent intent = new Intent(this, FlatShoppingListActivity.class);
		intent.putExtra("selection", this.productSelection);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	public void showTreeView(View source) {

	}

	@Override
	protected void onStart() {
		super.onStart();
		ExpandableListView categorizedProducts = (ExpandableListView) findViewById(R.id.productTree);
		for (Integer groupIndex : expandedGroups) {
			categorizedProducts.expandGroup(groupIndex);
		}
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
		productSelection = (ProductSelection) savedInstanceState.getSerializable("productSelection");
	}
}
