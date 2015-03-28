package net.binarysailor.shopping;

import net.binarysailor.shopping.catalog.CatalogEditActivity;
import net.binarysailor.shopping.catalog.dao.CatalogSQLiteHelper;
import net.binarysailor.shopping.common.DatabaseUtils;
import net.binarysailor.shopping.shoppinglist.ShoppingListTabbedActivity;
import net.binarysailor.shopping.shoppinglist.dao.ShoppingListSQLiteHelper;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		registerModuleSQLiteHelpers();
	}

	public void goToShopping(View b) {
		//Intent i = new Intent(this, ShoppingListEditActivity.class);
		Intent i = new Intent(this, ShoppingListTabbedActivity.class);
		startActivity(i);
	}

	public void goToCatalog(View b) {
		Intent i = new Intent(this, CatalogEditActivity.class);
		startActivity(i);
	}

	private void registerModuleSQLiteHelpers() {
		DatabaseUtils.registerModuleDBHelper(new CatalogSQLiteHelper());
		DatabaseUtils.registerModuleDBHelper(new ShoppingListSQLiteHelper());
	}

}
