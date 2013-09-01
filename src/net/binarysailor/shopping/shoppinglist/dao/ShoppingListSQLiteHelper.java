package net.binarysailor.shopping.shoppinglist.dao;

import net.binarysailor.shopping.common.ModuleSQLiteHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class ShoppingListSQLiteHelper implements ModuleSQLiteHelper {

	@Override
	public void setContext(Context context) {
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE shopping_list (" + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "name TEXT)");
		db.execSQL("CREATE TABLE enlisted_product (" + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "product_id INTEGER NOT NULL REFERENCES product(id) ON DELETE CASCADE, "
				+ "shopping_list_id INTEGER NOT NULL REFERENCES shopping_list(id) ON DELETE CASCADE, " + "quantity DECIMAL(10,2))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
	}

}
