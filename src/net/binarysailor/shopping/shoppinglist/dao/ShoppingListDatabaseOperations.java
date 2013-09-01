package net.binarysailor.shopping.shoppinglist.dao;

import java.util.Collection;

import net.binarysailor.shopping.common.DatabaseOperations;
import net.binarysailor.shopping.shoppinglist.model.EnlistedProduct;
import net.binarysailor.shopping.shoppinglist.model.ShoppingList;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

class ShoppingListDatabaseOperations {
	static class InsertShoppingList implements DatabaseOperations {

		private ShoppingList shoppingList;

		InsertShoppingList(ShoppingList shoppingList) {
			this.shoppingList = shoppingList;
		}

		@Override
		public Object execute(SQLiteDatabase db) {
			ContentValues cv = new ContentValues();
			cv.put(ShoppingListContract.ShoppingList.NAME, shoppingList.getName());
			long listId = db.insert(ShoppingListContract.ShoppingList.TABLE_NAME, null, cv);
			new InsertProducts((int) listId, shoppingList.getProducts()).execute(db);
			return (int) listId;
		}

	}

	static class UpdateShoppingListContents implements DatabaseOperations {

		private ShoppingList list;

		public UpdateShoppingListContents(ShoppingList list) {
			this.list = list;
		}

		@Override
		public Object execute(SQLiteDatabase db) {
			new DeleteEnlistedProducts(list.getId()).execute(db);
			new InsertProducts(list.getId(), list.getProducts()).execute(db);
			return null;
		}

	}

	static class DeleteShoppingList implements DatabaseOperations {

		int id;

		DeleteShoppingList(int id) {
			this.id = id;
		}

		@Override
		public Object execute(SQLiteDatabase db) {
			String[] idArray = new String[] { String.valueOf(id) };
			new DeleteEnlistedProducts(id).execute(db);
			db.delete(ShoppingListContract.ShoppingList.TABLE_NAME, ShoppingListContract.ShoppingList.ID + " = ?", idArray);
			return null;
		}
	}

	private static class DeleteEnlistedProducts implements DatabaseOperations {

		int listId;

		DeleteEnlistedProducts(int listId) {
			this.listId = listId;
		}

		@Override
		public Object execute(SQLiteDatabase db) {
			db.delete(ShoppingListContract.EnlistedProduct.TABLE_NAME, ShoppingListContract.EnlistedProduct.SHOPPING_LIST_ID + " = ?",
					new String[] { String.valueOf(listId) });
			return null;
		}
	}

	private static class InsertProducts implements DatabaseOperations {

		int listId;
		Collection<EnlistedProduct> products;

		InsertProducts(int listId, Collection<EnlistedProduct> products) {
			this.listId = listId;
			this.products = products;
		}

		@Override
		public Object execute(SQLiteDatabase db) {
			ContentValues cv = new ContentValues();
			cv.put(ShoppingListContract.EnlistedProduct.SHOPPING_LIST_ID, listId);
			for (EnlistedProduct product : products) {
				int productId = product.getProduct().getId();
				cv.put(ShoppingListContract.EnlistedProduct.PRODUCT_ID, productId);
				db.insert(ShoppingListContract.EnlistedProduct.TABLE_NAME, null, cv);
			}
			return null;
		}
	}
}
