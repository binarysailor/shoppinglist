package net.binarysailor.shopping.catalog.dao;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.binarysailor.shopping.catalog.model.Category;
import net.binarysailor.shopping.catalog.model.Product;
import net.binarysailor.shopping.common.DatabaseOperations;
import net.binarysailor.shopping.common.DatabaseUtils;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.SparseArray;

public class CatalogDAO {
	private static String[] CATEGORY_COLUMNS = { CatalogContract.Category.ID, CatalogContract.Category.NAME };

	private static String[] PRODUCT_COLUMNS = { CatalogContract.Product.ID, CatalogContract.Product.NAME,
			CatalogContract.Product.CATEGORY_ID };

	private static List<Category> categories;
	private static SparseArray<Product> productsById;

	private Context context;

	public CatalogDAO(Context context) {
		this.context = context;
	}

	public List<Category> getCategories() {
		assertCategoriesLoaded();
		return CatalogDAO.categories;
	}

	public Category getCategoryById(int id) {
		for (Category c : getCategories()) {
			if (c.getId() == id) {
				return c;
			}
		}
		return null;
	}

	public void deleteCategory(final int id) {
		Iterator<Category> c = getCategories().iterator();
		while (c.hasNext()) {
			Category category = c.next();
			if (category.getId() == id) {
				DatabaseUtils.executeInTransaction(context, new DatabaseOperations() {
					@Override
					public Object execute(SQLiteDatabase db) {
						String[] queryArgs = new String[] { String.valueOf(id) };
						ContentValues values = new ContentValues();
						values.put("active", false);
						db.update(CatalogContract.Category.TABLE_NAME, values, CatalogContract.Category.ID + " = ?",
								queryArgs);
						return null;
					}
				});
				c.remove();
			}
		}
	}

	public void updateCategory(final Category editedCategory) {
		for (Category c : getCategories()) {
			if (c.getId() == editedCategory.getId()) {
				DatabaseUtils.executeInTransaction(context, new DatabaseOperations() {
					@Override
					public Object execute(SQLiteDatabase db) {
						ContentValues values = new ContentValues();
						values.put(CatalogContract.Category.NAME, editedCategory.getName());
						db.update(CatalogContract.Category.TABLE_NAME, values, CatalogContract.Category.ID + " = ?",
								new String[] { String.valueOf(editedCategory.getId()) });
						return null;
					}
				});
				c.setName(editedCategory.getName());
				break;
			}
		}
	}

	private void assertCategoriesLoaded() {
		if (CatalogDAO.categories == null) {
			loadCategories();
		}
	}

	private void loadCategories() {
		SQLiteDatabase db = DatabaseUtils.getReadableDatabase(context);
		Cursor cursor = db.query(CatalogContract.Category.TABLE_NAME, CATEGORY_COLUMNS, CatalogContract.Category.ACTIVE
				+ " = 1", null, null, null, CatalogContract.Category.SORT_ORDER);
		List<Category> categories = new LinkedList<Category>();
		SparseArray<Category> categoriesById = new SparseArray<Category>();
		boolean dataAvailable = cursor.moveToFirst();
		while (dataAvailable) {
			Category category = EntityFactory.createCategory(cursor);
			categories.add(category);
			categoriesById.put(category.getId(), category);
			dataAvailable = cursor.moveToNext();
		}
		cursor.close();
		CatalogDAO.productsById = new SparseArray<Product>();
		cursor = db.query(CatalogContract.Product.TABLE_NAME, PRODUCT_COLUMNS, null, null, null, null,
				CatalogContract.Product.CATEGORY_ID + "," + CatalogContract.Product.SORT_ORDER);
		dataAvailable = cursor.moveToFirst();
		while (dataAvailable) {
			Product product = EntityFactory.createProduct(cursor);
			Category c = categoriesById.get(product.getCategory().getId());
			if (c != null) {
				c.addProduct(product);
				CatalogDAO.productsById.put(product.getId(), product);
			}
			dataAvailable = cursor.moveToNext();
		}
		cursor.close();
		db.close();
		CatalogDAO.categories = categories;
	}

	public void createCategory(final Category editedCategory) {
		assertCategoriesLoaded();
		int categoryId = (Integer) DatabaseUtils.executeInTransaction(context, new DatabaseOperations() {
			@Override
			public Object execute(SQLiteDatabase db) {
				ContentValues values = new ContentValues();
				values.put(CatalogContract.Category.NAME, editedCategory.getName());
				values.put(CatalogContract.Category.SORT_ORDER, generateSortOrder());
				values.put(CatalogContract.Category.ACTIVE, true);
				long rowid = db.insert(CatalogContract.Category.TABLE_NAME, null, values);
				Cursor idCursor = db.rawQuery("select " + CatalogContract.Category.ID + " from "
						+ CatalogContract.Category.TABLE_NAME + " where ROWID = ?",
						new String[] { String.valueOf(rowid) });
				idCursor.moveToFirst();
				return idCursor.getInt(0);
			}
		});
		editedCategory.setId(categoryId);
		CatalogDAO.categories.add(editedCategory);
	}

	public void createProduct(final Product editedProduct) {
		int productId = (Integer) DatabaseUtils.executeInTransaction(context, new DatabaseOperations() {
			@Override
			public Object execute(SQLiteDatabase db) {
				ContentValues values = new ContentValues();
				values.put(CatalogContract.Product.CATEGORY_ID, editedProduct.getCategory().getId());
				values.put(CatalogContract.Product.NAME, editedProduct.getName());
				values.put(CatalogContract.Product.SORT_ORDER, generateSortOrder());
				long rowid = db.insert(CatalogContract.Product.TABLE_NAME, null, values);
				Cursor idCursor = db.rawQuery("select " + CatalogContract.Product.ID + " from "
						+ CatalogContract.Product.TABLE_NAME + " where ROWID = ?",
						new String[] { String.valueOf(rowid) });
				idCursor.moveToFirst();
				return idCursor.getInt(0);
			}
		});
		editedProduct.setId(productId);
		Category category = getCategoryById(editedProduct.getCategory().getId());
		category.addProduct(editedProduct);
	}

	public void updateProduct(final Product editedProduct) {
		Product product = CatalogDAO.productsById.get(editedProduct.getId());
		if (product != null) {
			DatabaseUtils.executeInTransaction(context, new DatabaseOperations() {
				@Override
				public Object execute(SQLiteDatabase db) {
					ContentValues values = new ContentValues();
					values.put(CatalogContract.Product.NAME, editedProduct.getName());
					db.update(CatalogContract.Product.TABLE_NAME, values, CatalogContract.Product.ID + " = ?",
							new String[] { String.valueOf(editedProduct.getId()) });
					return null;
				}
			});
			product.setName(editedProduct.getName());
		}
	}

	private String generateSortOrder() {
		return "z" + System.currentTimeMillis();
	}
}
