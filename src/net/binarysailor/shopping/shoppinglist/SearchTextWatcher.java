package net.binarysailor.shopping.shoppinglist;

import net.binarysailor.shopping.R;
import net.binarysailor.shopping.catalog.CatalogViewAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ExpandableListView;

public class SearchTextWatcher implements TextWatcher {

	private ShoppingListEditActivity shoppingListEdit;

	SearchTextWatcher(ShoppingListEditActivity shoppingListEdit) {
		this.shoppingListEdit = shoppingListEdit;
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
	}

	@Override
	public void afterTextChanged(Editable s) {
		ExpandableListView categorizedProducts = (ExpandableListView) shoppingListEdit.findViewById(R.id.productTree);
		CatalogViewAdapter adapter = (CatalogViewAdapter) categorizedProducts.getExpandableListAdapter();
		String text = s.toString();
		adapter.setFilterText(text);
		if (text.trim().equals("")) {
			shoppingListEdit.restoreExpandCollapseState();
		} else {
			shoppingListEdit.expandAll();
		}
	}
}
