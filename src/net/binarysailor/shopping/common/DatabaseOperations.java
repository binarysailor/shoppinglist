package net.binarysailor.shopping.common;

import android.database.sqlite.SQLiteDatabase;

public interface DatabaseOperations {
	Object execute(SQLiteDatabase db);
}
