package net.binarysailor.shopping.shoppinglist;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.binarysailor.shopping.R;
import net.binarysailor.shopping.catalog.CatalogViewAdapter;
import net.binarysailor.shopping.catalog.dao.CatalogDAO;
import net.binarysailor.shopping.catalog.model.Category;
import net.binarysailor.shopping.catalog.model.Product;
import net.binarysailor.shopping.shoppinglist.dao.ShoppingListDAO;
import net.binarysailor.shopping.shoppinglist.model.ProductSelection;
import net.binarysailor.shopping.shoppinglist.model.ProductSelectionFactory;
import net.binarysailor.shopping.shoppinglist.model.ShoppingList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;

public class ShoppingListEditActivity extends Activity {

	ProductSelection productSelection;
	ShoppingList loadedList;
	Set<Integer> expandedGroups = new HashSet<Integer>();
	TreeGroupExpandListener expandListener = new TreeGroupExpandListener(expandedGroups);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping);
		productSelection = ProductSelectionFactory.getProductSelection(getIntent());

		List<Category> categories = new CatalogDAO(this).getCategories();
		setupCatalogView(categories);

		EditText searchField = (EditText) findViewById(R.id.searchText);
		searchField.addTextChangedListener(new SearchTextWatcher(this));
		searchField.clearFocus();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.shopping_list_edit, menu);
		// manageMenuSaveOption(menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.findItem(R.id.save_current_list).setEnabled(loadedList != null);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.load_list: {
			Intent intent = new Intent(this, ShoppingListsActivity.class);
			startActivityForResult(intent, 0);
			break;
		}
		case R.id.save_current_list:
			if (loadedList != null) {
				new ShoppingListDAO(this).updateShoppingListContents(loadedList.getId(), productSelection);
				resetModifiedMarker();
				break;
			}
		case R.id.save_current_list_as:
			openSaveAsDialog();
			break;
		case R.id.add_non_catalog_product:
			addNonCatalogProduct("Any product");
		}
		return true;
	}

	public void addNonCatalogProduct(String name) {
		CatalogViewAdapter adapter = (CatalogViewAdapter) getCatalogView().getExpandableListAdapter();
		adapter.addNonCatalogProduct(name);
		setModifiedMarker();
	}

	public void setModifiedMarker() {
		if (!getTitle().toString().endsWith(" *")) {
			setTitle(getTitle() + " *");
		}
	}

	public void resetModifiedMarker() {
		if (getTitle().toString().endsWith(" *")) {
			setTitle(getTitle().toString().substring(0, getTitle().length() - 2));
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_CANCELED) {
			SavedShoppingListCommand command = (SavedShoppingListCommand) data.getSerializableExtra("command");
			command.execute(this);
			expandSelectedCollapseDeselected();
		}
	}

	void saveListAs(String name) {
		ShoppingList list = new ShoppingList();
		list.setName(name);
		for (Product product : new CatalogDAO(this).getAllProducts()) {
			if (productSelection.isSelected(product.getId())) {
				list.enlistProduct(product, BigDecimal.ONE);
			}
		}
		ShoppingList createdShoppingList = new ShoppingListDAO(this).createShoppingList(list);
		loadList(createdShoppingList);
	}

	private void openSaveAsDialog() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		EditText edit = new EditText(this);
		edit.setId(android.R.id.text1);
		edit.setHint("Lista");
		SaveAsNameWatcher saveAsNameWatcher = new SaveAsNameWatcher();
		edit.addTextChangedListener(saveAsNameWatcher);
		dialogBuilder.setTitle("Wprowadz nazwe listy").setView(edit).setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				EditText edit = (EditText) ((AlertDialog) dialog).findViewById(android.R.id.text1);
				saveListAs(edit.getText().toString());
			}
		});
		AlertDialog dialog = dialogBuilder.create();
		saveAsNameWatcher.setDialog(dialog);
		dialog.show();
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
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
			setModifiedMarker();
		}
	}

	public void productNameClicked(View v) {
		CheckBox checkBox = (CheckBox) (((ViewGroup) v.getParent()).getChildAt(0));
		checkBox.toggle();
		productCheckboxClicked(checkBox);
	}

	private View setupCatalogView(List<Category> categories) {
		ExpandableListView categorizedProducts = (ExpandableListView) findViewById(R.id.productTree);
		ShoppingListEditProductViewFactory productViewFactory = new ShoppingListEditProductViewFactory(this.productSelection);
		CatalogViewAdapter adapter = new CatalogViewAdapter(this, productViewFactory);
		categorizedProducts.setAdapter(adapter);
		categorizedProducts.setOnGroupExpandListener(expandListener);
		categorizedProducts.setOnGroupCollapseListener(expandListener);
		return categorizedProducts;
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

	void restoreExpandCollapseState() {
		expandListener.stopListening();
		ExpandableListView categorizedProducts = getCatalogView();
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

	void expandSelectedCollapseDeselected() {
		ExpandableListView categorizedProducts = getCatalogView();
		CatalogViewAdapter cva = (CatalogViewAdapter) categorizedProducts.getExpandableListAdapter();
		int size = cva.getGroupCount();
		for (int i = 0; i < size; i++) {
			Category category = (Category) cva.getGroup(i);
			boolean anythingSelected = false;
			for (Product product : category.getProducts()) {
				if (productSelection.isSelected(product.getId())) {
					categorizedProducts.expandGroup(i);
					anythingSelected = true;
					break;
				}
			}
			if (!anythingSelected) {
				categorizedProducts.collapseGroup(i);
			}
		}
	}

	void expandAll() {
		ExpandableListView categorizedProducts = getCatalogView();
		CatalogViewAdapter adapter = (CatalogViewAdapter) categorizedProducts.getExpandableListAdapter();
		int groupCount = adapter.getGroupCount();
		expandListener.stopListening();
		for (int i = 0; i < groupCount; i++) {
			categorizedProducts.expandGroup(i);
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

	public ProductSelection getProductSelection() {
		return productSelection;
	}

	public void loadList(ShoppingList shoppingList) {
		this.loadedList = shoppingList;
		productSelection.deselectAll();
		productSelection.add(shoppingList.toSelection());
		setTitle(shoppingList.getName());
	}

	private ExpandableListView getCatalogView() {
		return (ExpandableListView) findViewById(R.id.productTree);
	}
}
