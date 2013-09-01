package net.binarysailor.shopping.common;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseUtils {

	static final int DB_VERSION = 3;
	static List<ModuleSQLiteHelper> moduleHelpers = new LinkedList<ModuleSQLiteHelper>();

	public static void registerModuleDBHelper(ModuleSQLiteHelper helper) {
		moduleHelpers.add(helper);
	}

	public static SQLiteDatabase getReadableDatabase(Context context) {
		ShoppingOpenHelper helper = new ShoppingOpenHelper(context, DB_VERSION, moduleHelpers);
		return helper.getReadableDatabase();
	}

	public static SQLiteDatabase getWritableDatabase(Context context) {
		ShoppingOpenHelper helper = new ShoppingOpenHelper(context, DB_VERSION, moduleHelpers);
		return helper.getWritableDatabase();
	}

	public static Object executeInTransaction(Context context, DatabaseOperations ops) {
		SQLiteDatabase db = getWritableDatabase(context);
		db.beginTransaction();
		Object result = ops.execute(db);
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
		return result;
	}
}
