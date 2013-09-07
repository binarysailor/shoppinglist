package net.binarysailor.shopping.shoppinglist;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.binarysailor.shopping.R;
import net.binarysailor.shopping.catalog.dao.CatalogDAO;
import net.binarysailor.shopping.catalog.model.Category;
import net.binarysailor.shopping.catalog.model.Product;
import net.binarysailor.shopping.common.ui.SingleTextFieldDialog;
import net.binarysailor.shopping.common.ui.SingleTextFieldDialogCallback;
import net.binarysailor.shopping.common.ui.SingleTextFieldDialogOptions;
import net.binarysailor.shopping.shoppinglist.dao.ShoppingListDAO;
import net.binarysailor.shopping.shoppinglist.model.EnlistedProduct;
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
			openNonCatalogProductDialog();
			break;
		case R.id.new_list:
			resetList();
			break;
		}
		return true;
	}

	private void openNonCatalogProductDialog() {
		SingleTextFieldDialogCallback callback = new SingleTextFieldDialogCallback() {
			@Override
			public void onOK(String text) {
				addNonCatalogProduct(text);

			}
		};
		SingleTextFieldDialogOptions options = new SingleTextFieldDialogOptions(getString(R.string.add_non_catalog_product_title),
				getString(R.string.add_non_catalog_product_prompt), callback);
		new SingleTextFieldDialog(this, options).open();
	}

	public void addNonCatalogProduct(String name) {
		Product product = new Product();
		product.setName(name);
		new CatalogDAO(this).createProduct(product);
		productSelection.select(product.getId());
		CatalogViewAdapter adapter = getCatalogViewAdapter();
		adapter.addNonCatalogProduct(product);
		setModifiedMarker();
		expandSelectedCollapseDeselected();
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
		loadList(createdShoppingList, true);
	}

	private void openSaveAsDialog() {
		SingleTextFieldDialogCallback callback = new SingleTextFieldDialogCallback() {
			@Override
			public void onOK(String text) {
				saveListAs(text);
			}
		};
		SingleTextFieldDialogOptions options = new SingleTextFieldDialogOptions(getString(R.string.save_list_as_title),
				getString(R.string.save_list_as_prompt), callback);
		new SingleTextFieldDialog(this, options).open();
	}

	public void clearSearchFilterClicked(View v) {
		EditText searchText = (EditText) findViewById(R.id.searchText);
		searchText.setText("");
	}

	public void productCheckboxClicked(View v) {
		if (v instanceof CheckBox) {
			final CheckBox cb = (CheckBox) v;
			final Product product = (Product) cb.getTag();
			if (cb.isChecked()) {
				productSelection.select(product.getId());
			} else {
				if (product.isInCatalog()) {
					productSelection.deselect(product.getId());
				} else {
					AlertDialog dialog = new AlertDialog.Builder(this).create();
					dialog.setTitle("Na pewno?");
					dialog.setMessage("Odznaczenie tego produktu spowoduje jego usuniecie");
					dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
							cb.setChecked(true);
						}
					});
					dialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							productSelection.deselect(product.getId());
							getCatalogViewAdapter().removeNonCatalogProduct(product);
						}
					});
					dialog.show();
				}
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
		CatalogViewAdapter adapter = new CatalogViewAdapter(this, productSelection);
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
		CatalogViewAdapter cva = getCatalogViewAdapter();
		int size = cva.getGroupCount();
		for (int i = 0; i < size; i++) {
			ProductGroup category = (ProductGroup) cva.getGroup(i);
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
		CatalogViewAdapter adapter = getCatalogViewAdapter();
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

	public void loadList(ShoppingList shoppingList, boolean replace) {
		CatalogViewAdapter catalogViewAdapter = getCatalogViewAdapter();
		if (replace) {
			this.loadedList = shoppingList;
			setTitle(shoppingList.getName());
			productSelection.deselectAll();
			catalogViewAdapter.clearNonCatalogProducts();
		} else {
			setModifiedMarker();
		}
		productSelection.add(shoppingList.toSelection());
		addNonCatalogProducts(shoppingList);
		catalogViewAdapter.notifyDataSetChanged();
	}

	private void addNonCatalogProducts(ShoppingList shoppingList) {
		CatalogViewAdapter catalogViewAdapter = getCatalogViewAdapter();
		for (EnlistedProduct ep : shoppingList.getNonCatalogProducts()) {
			catalogViewAdapter.addNonCatalogProduct(ep.getProduct());
		}
	}

	public void resetList() {
		this.loadedList = null;
		productSelection.deselectAll();
		getCatalogViewAdapter().notifyDataSetChanged();
		setTitle(getString(R.string.title_activity_shopping));
		expandSelectedCollapseDeselected();
	}

	private ExpandableListView getCatalogView() {
		return (ExpandableListView) findViewById(R.id.productTree);
	}

	private CatalogViewAdapter getCatalogViewAdapter() {
		return ((CatalogViewAdapter) getCatalogView().getExpandableListAdapter());
	}
}
