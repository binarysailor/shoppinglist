package net.binarysailor.shopping.catalog;

import net.binarysailor.shopping.R;
import net.binarysailor.shopping.catalog.dao.CatalogDAO;
import net.binarysailor.shopping.catalog.model.Category;
import net.binarysailor.shopping.catalog.model.Product;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;

public class CatalogEditActivity extends Activity {

	static final int REQ_CODE_EDIT_CATEGORY = 1;
	static final int REQ_CODE_EDIT_PRODUCT = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_catalog_edit);
		drawProductTree();
	}

	public void editCategory(View editButton) {
		final Category category = (Category) editButton.getTag();
		Intent i = new Intent(this, CategoryEditActivity.class);
		i.putExtra("category", category);
		startActivityForResult(i, REQ_CODE_EDIT_CATEGORY);
	}

	public void newCategory(View button) {
		Intent i = new Intent(this, CategoryEditActivity.class);
		i.putExtra("category", new Category());
		startActivityForResult(i, REQ_CODE_EDIT_CATEGORY);
	}

	public void confirmDeleteCategory(View deleteButton) {
		Category category = (Category) deleteButton.getTag();
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(android.R.string.yes),
				new CategoryDeleteConfirmClickListener(this, category));
		dialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(android.R.string.no), new DeleteCancelClickListener());
		dialog.setTitle(R.string.catalog_edit_deleteCategory_confirmation_title);
		dialog.setMessage(getString(R.string.catalog_edit_deleteCategory_confirmation_message, category.getName()));
		dialog.show();
	}

	public void editProduct(View editButton) {
		Product product = (Product) editButton.getTag();
		Intent i = new Intent(this, ProductEditActivity.class);
		i.putExtra("product", product);
		startActivityForResult(i, REQ_CODE_EDIT_PRODUCT);
	}

	public void newProduct(View button) {
		Category category = (Category) button.getTag();
		Product product = new Product();
		product.setCategory(category);
		Intent i = new Intent(this, ProductEditActivity.class);
		i.putExtra("product", product);
		startActivityForResult(i, REQ_CODE_EDIT_PRODUCT);
	}

	public void confirmDeleteProduct(View deleteButton) {
		Product product = (Product) deleteButton.getTag();
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(android.R.string.yes),
				new ProductDeleteConfirmClickListener(this, product));
		dialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(android.R.string.no), new DeleteCancelClickListener());
		dialog.setTitle(R.string.catalog_edit_deleteProduct_confirmation_title);
		dialog.setMessage(getString(R.string.catalog_edit_deleteProduct_confirmation_message, product.getName()));
		dialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQ_CODE_EDIT_CATEGORY:
			if (resultCode == RESULT_OK) {
				Category editedCategory = (Category) data.getSerializableExtra("category");
				CatalogDAO catalogDAO = new CatalogDAO(this);
				if (editedCategory.isNew()) {
					catalogDAO.createCategory(editedCategory);
				} else {
					catalogDAO.updateCategory(editedCategory);
				}
				refreshProductTree();
			}
			break;
		case REQ_CODE_EDIT_PRODUCT:
			if (resultCode == RESULT_OK) {
				Product editedProduct = (Product) data.getSerializableExtra("product");
				CatalogDAO catalogDAO = new CatalogDAO(this);
				if (editedProduct.isNew()) {
					catalogDAO.createProduct(editedProduct);
				} else {
					catalogDAO.updateProduct(editedProduct);
				}
				refreshProductTree();
			}
			break;
		default:
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	void refreshProductTree() {
		ExpandableListView categorizedProducts = (ExpandableListView) findViewById(R.id.productTree);
		((BaseAdapter) categorizedProducts.getAdapter()).notifyDataSetChanged();
	}

	private void drawProductTree() {
		ExpandableListView categorizedProducts = (ExpandableListView) findViewById(R.id.productTree);
		CatalogEditViewFactory productViewFactory = new CatalogEditViewFactory();
		categorizedProducts.setAdapter(new CatalogViewAdapter(this, productViewFactory));
	}

}
