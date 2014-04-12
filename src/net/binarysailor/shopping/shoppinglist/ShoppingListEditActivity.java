package net.binarysailor.shopping.shoppinglist;

import java.math.BigDecimal;
import java.util.List;

import net.binarysailor.shopping.R;
import net.binarysailor.shopping.catalog.dao.CatalogDAO;
import net.binarysailor.shopping.catalog.model.Category;
import net.binarysailor.shopping.catalog.model.Product;
import net.binarysailor.shopping.common.ui.SingleTextFieldDialog;
import net.binarysailor.shopping.common.ui.SingleTextFieldDialogCallback;
import net.binarysailor.shopping.common.ui.SingleTextFieldDialogOptions;
import net.binarysailor.shopping.shoppinglist.dao.ShoppingListDAO;
import net.binarysailor.shopping.shoppinglist.model.EnlistedProduct;
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

	private static final int REQUEST_CODE_LOAD_LIST = 0;

	private CurrentShoppingList currentList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping);
		currentList = CurrentShoppingList.getInstance();

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
		menu.findItem(R.id.save_current_list).setEnabled(currentList.isListLoaded());
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.load_list: {
			Intent intent = new Intent(this, ShoppingListsActivity.class);
			startActivityForResult(intent, REQUEST_CODE_LOAD_LIST);
			break;
		}
		case R.id.save_current_list:
			if (currentList.isListLoaded()) {
				saveCurrentList();
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

	private void saveCurrentList() {
		new ShoppingListDAO(this).updateShoppingListContents(currentList.getLoadedListId(), currentList.getProductSelection());
		resetModifiedMarker();
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
		currentList.selectProduct(product.getId());
		CatalogViewAdapter adapter = getCatalogViewAdapter();
		adapter.addNonCatalogProduct(product);
		setModifiedMarker();
		expandSelectedCollapseDeselected();
	}

	public void setModifiedMarker() {
		currentList.setListModified(true);
		updateTitle();
	}

	public void resetModifiedMarker() {
		currentList.setListModified(false);
		updateTitle();
	}

	private void updateTitle() {
		if (currentList.isListModified()) {
			if (!getTitle().toString().endsWith(" *")) {
				setTitle(getTitle() + " *");
			}
		} else {
			if (getTitle().toString().endsWith(" *")) {
				setTitle(getTitle().toString().substring(0, getTitle().length() - 2));
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_CODE_LOAD_LIST:
			if (resultCode != RESULT_CANCELED) {
				SavedShoppingListCommand command = (SavedShoppingListCommand) data.getSerializableExtra("command");
				command.execute(this);
				expandSelectedCollapseDeselected();
			}
			break;
		}
	}

	void saveListAs(String name) {
		ShoppingList list = new ShoppingList();
		list.setName(name);
		for (Product product : new CatalogDAO(this).getAllProducts()) {
			if (currentList.isProductSelected(product.getId())) {
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
				currentList.selectProduct(product.getId());
			} else {
				if (product.isInCatalog()) {
					currentList.deselectProduct(product.getId());
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
							currentList.deselectProduct(product.getId());
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
		CatalogViewAdapter adapter = new CatalogViewAdapter(this, currentList.getProductSelection());
		categorizedProducts.setAdapter(adapter);
		categorizedProducts.setOnGroupExpandListener(currentList);
		categorizedProducts.setOnGroupCollapseListener(currentList);
		return categorizedProducts;
	}

	public void showFlatView(View source) {
		Intent intent = new Intent(this, FlatShoppingListActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@Override
	protected void onStart() {
		super.onStart();
		updateTitle();
		getCatalogViewAdapter().notifyDataSetChanged();
		restoreExpandCollapseState();
	}

	void restoreExpandCollapseState() {
		currentList.stopListening();
		ExpandableListView categorizedProducts = getCatalogView();
		int size = categorizedProducts.getExpandableListAdapter().getGroupCount();
		for (int i = 0; i < size; i++) {
			if (currentList.isGroupExpanded(i)) {
				categorizedProducts.expandGroup(i);
			} else {
				categorizedProducts.collapseGroup(i);
			}
		}
		currentList.startListening();
	}

	void expandSelectedCollapseDeselected() {
		ExpandableListView categorizedProducts = getCatalogView();
		CatalogViewAdapter cva = getCatalogViewAdapter();
		int size = cva.getGroupCount();
		for (int i = 0; i < size; i++) {
			ProductGroup category = (ProductGroup) cva.getGroup(i);
			boolean anythingSelected = false;
			for (Product product : category.getProducts()) {
				if (currentList.isProductSelected(product.getId())) {
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
		currentList.stopListening();
		for (int i = 0; i < groupCount; i++) {
			categorizedProducts.expandGroup(i);
		}
		currentList.startListening();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("currentList", currentList);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		currentList = (CurrentShoppingList) savedInstanceState.getSerializable("currentList");
	}

	public void loadList(ShoppingList shoppingList, boolean replace) {
		CatalogViewAdapter catalogViewAdapter = getCatalogViewAdapter();
		currentList.loadList(shoppingList, replace);
		if (replace) {
			setTitle(shoppingList.getName());
			catalogViewAdapter.clearNonCatalogProducts();
		} else {
			setModifiedMarker();
		}
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
		currentList.reset();
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
