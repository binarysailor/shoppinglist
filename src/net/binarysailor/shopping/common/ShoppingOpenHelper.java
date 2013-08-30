package net.binarysailor.shopping.common;

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class ShoppingOpenHelper extends SQLiteOpenHelper {

	private Context context;
	private List<ModuleSQLiteHelper> moduleSQLiteHelpers;

	public ShoppingOpenHelper(Context context, int version, List<ModuleSQLiteHelper> moduleSQLiteHelpers) {
		super(context, "shopping", null, version);
		this.context = context;
		this.moduleSQLiteHelpers = moduleSQLiteHelpers;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for (ModuleSQLiteHelper helper : moduleSQLiteHelpers) {
			helper.setContext(context);
			helper.onCreate(db);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
		for (ModuleSQLiteHelper helper : moduleSQLiteHelpers) {
			helper.setContext(context);
			helper.onUpgrade(db, oldVer, newVer);
		}
	}
}
