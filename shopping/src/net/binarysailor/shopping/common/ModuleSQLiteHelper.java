package net.binarysailor.shopping.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public interface ModuleSQLiteHelper {

	void setContext(Context context);

	void onCreate(SQLiteDatabase db);

	void onUpgrade(SQLiteDatabase db, int oldVer, int newVer);

}
