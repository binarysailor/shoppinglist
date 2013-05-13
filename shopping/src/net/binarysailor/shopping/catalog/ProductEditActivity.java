package net.binarysailor.shopping.catalog;

import net.binarysailor.shopping.R;
import net.binarysailor.shopping.catalog.model.Product;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ProductEditActivity extends Activity {

	private Product product;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		product = (Product) getIntent().getSerializableExtra("product");

		setContentView(R.layout.activity_product_edit);

		TextView categoryName = (TextView) findViewById(R.id.category_name);
		categoryName.setText(product.getCategory().getName());
		EditText productNameTextField = getProductNameTextField();
		if (!product.isNew()) {
			productNameTextField.setText(product.getName());
		}
		productNameTextField.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				enableOrDisableOKButton();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		enableOrDisableOKButton();
	}

	private void enableOrDisableOKButton() {
		boolean enable = true;
		if (getProductNameTextField().getText().toString().trim().length() == 0) {
			enable = false;
		}
		findViewById(R.id.saveButton).setEnabled(enable);
	}

	public void saveProduct(View button) {
		Intent resultIntent = new Intent();
		Product editedProduct = new Product();
		editedProduct.setId(product.getId());
		editedProduct.setCategory(product.getCategory());
		editedProduct.setName(getProductNameTextField().getText().toString().trim());
		resultIntent.putExtra("product", editedProduct);
		setResult(RESULT_OK, resultIntent);
		finish();
	}

	public void cancel(View button) {
		setResult(RESULT_CANCELED);
		finish();
	}

	private EditText getProductNameTextField() {
		return (EditText) findViewById(R.id.name_field);
	}
}
