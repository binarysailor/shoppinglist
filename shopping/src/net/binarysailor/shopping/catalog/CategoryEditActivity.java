package net.binarysailor.shopping.catalog;

import net.binarysailor.shopping.R;
import net.binarysailor.shopping.catalog.model.Category;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

public class CategoryEditActivity extends Activity {

	private Category category;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		category = (Category) getIntent().getSerializableExtra("category");
		setContentView(R.layout.activity_category_edit);
		EditText categoryNameTextField = getCategoryNameTextField();
		if (!category.isNew()) {
			categoryNameTextField.setText(category.getName());
		}
		categoryNameTextField.addTextChangedListener(new TextWatcher() {

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
		if (getCategoryNameTextField().getText().toString().trim().length() == 0) {
			enable = false;
		}
		findViewById(R.id.saveButton).setEnabled(enable);
	}

	public void saveCategory(View button) {
		Intent resultIntent = new Intent();
		Category editedCategory = new Category();
		editedCategory.setId(category.getId());
		editedCategory.setName(getCategoryNameTextField().getText().toString().trim());
		resultIntent.putExtra("category", editedCategory);
		setResult(RESULT_OK, resultIntent);
		finish();
	}

	public void cancel(View button) {
		setResult(RESULT_CANCELED);
		finish();
	}

	private EditText getCategoryNameTextField() {
		return (EditText) findViewById(R.id.name_field);
	}
}
