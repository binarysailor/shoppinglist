package net.binarysailor.shopping.catalog.dao;

import net.binarysailor.shopping.catalog.model.Category;
import net.binarysailor.shopping.catalog.model.Product;
import net.binarysailor.shopping.common.DatabaseOperations;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

class CatalogDatabaseOperations {

	static class InsertCategory implements DatabaseOperations {

		private Category categoryToInsert;

		InsertCategory(Category categoryToInsert) {
			this.categoryToInsert = categoryToInsert;
		}

		@Override
		public Object execute(SQLiteDatabase db) {
			ContentValues values = new ContentValues();
			values.put(CatalogContract.Category.NAME, categoryToInsert.getName());
			values.put(CatalogContract.Category.SORT_ORDER, CatalogDatabaseOperations.generateSortOrder());
			values.put(CatalogContract.Category.ACTIVE, true);
			long rowid = db.insert(CatalogContract.Category.TABLE_NAME, null, values);
			return (int) rowid;
		}
	}

	static class UpdateCategory implements DatabaseOperations {
		private Category categoryToUpdate;

		UpdateCategory(Category categoryToUpdate) {
			this.categoryToUpdate = categoryToUpdate;
		}

		@Override
		public Object execute(SQLiteDatabase db) {
			ContentValues values = new ContentValues();
			values.put(CatalogContract.Category.NAME, categoryToUpdate.getName());
			db.update(CatalogContract.Category.TABLE_NAME, values, CatalogContract.Category.ID + " = ?",
					new String[] { String.valueOf(categoryToUpdate.getId()) });
			return null;
		}
	}

	static class DeleteCategory implements DatabaseOperations {

		private int categoryId;

		DeleteCategory(int categoryId) {
			this.categoryId = categoryId;
		}

		@Override
		public Object execute(SQLiteDatabase db) {
			String[] queryArgs = new String[] { String.valueOf(categoryId) };
			ContentValues values = new ContentValues();
			values.put("active", false);
			db.update(CatalogContract.Category.TABLE_NAME, values, CatalogContract.Category.ID + " = ?", queryArgs);
			return null;
		}

	}

	static class InsertProduct implements DatabaseOperations {

		private Product productToInsert;

		InsertProduct(Product productToInsert) {
			this.productToInsert = productToInsert;
		}

		@Override
		public Object execute(SQLiteDatabase db) {
			ContentValues values = new ContentValues();
			values.put(CatalogContract.Product.CATEGORY_ID, productToInsert.getCategory().getId());
			values.put(CatalogContract.Product.NAME, productToInsert.getName());
			values.put(CatalogContract.Product.SORT_ORDER, CatalogDatabaseOperations.generateSortOrder());
			values.put(CatalogContract.Product.ACTIVE, true);
			long rowid = db.insert(CatalogContract.Product.TABLE_NAME, null, values);
			return (int) rowid;
		}
	}

	static class UpdateProduct implements DatabaseOperations {

		private Product productToUpdate;

		UpdateProduct(Product productToUpdate) {
			this.productToUpdate = productToUpdate;
		}

		@Override
		public Object execute(SQLiteDatabase db) {
			ContentValues values = new ContentValues();
			values.put(CatalogContract.Product.NAME, productToUpdate.getName());
			db.update(CatalogContract.Product.TABLE_NAME, values, CatalogContract.Product.ID + " = ?",
					new String[] { String.valueOf(productToUpdate.getId()) });
			return null;
		}
	}

	static class DeleteProduct implements DatabaseOperations {
		private int productId;

		DeleteProduct(int productId) {
			this.productId = productId;
		}

		@Override
		public Object execute(SQLiteDatabase db) {
			String[] queryArgs = new String[] { String.valueOf(productId) };
			ContentValues values = new ContentValues();
			values.put("active", false);
			db.update(CatalogContract.Product.TABLE_NAME, values, CatalogContract.Product.ID + " = ?", queryArgs);
			return null;
		}

	}

	static String generateSortOrder() {
		return "z" + System.currentTimeMillis();
	}

}
