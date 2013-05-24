package net.binarysailor.shopping.common;

import java.util.LinkedList;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;

public class DatabaseOperationsChain implements DatabaseOperations {

	private List<DatabaseOperations> chain = new LinkedList<DatabaseOperations>();

	public void add(DatabaseOperations operations) {
		chain.add(operations);
	}

	@Override
	public Object execute(SQLiteDatabase db) {
		Object result = null;
		for (DatabaseOperations o : chain) {
			result = o.execute(db);
		}
		return result;
	}
}
