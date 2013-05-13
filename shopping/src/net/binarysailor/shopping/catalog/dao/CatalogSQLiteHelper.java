package net.binarysailor.shopping.catalog.dao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.binarysailor.shopping.R;
import net.binarysailor.shopping.catalog.model.Category;
import net.binarysailor.shopping.catalog.model.Product;
import net.binarysailor.shopping.common.ModuleSQLiteHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class CatalogSQLiteHelper implements ModuleSQLiteHelper {

	private Context context;

	@Override
	public void setContext(Context context) {
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE category (" + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "name TEXT, "
				+ "sort_order TEXT, active BOOLEAN)");
		db.execSQL("CREATE TABLE product (" + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "name TEXT, "
				+ "category_id INTEGER REFERENCES category(id) ON DELETE SET NULL," + "sort_order TEXT)");
		Map<Category, List<Product>> categories2Products = getInitialCategories2Products();
		for (Category category : categories2Products.keySet()) {
			long categoryId = insertCategory(db, category);
			for (Product product : categories2Products.get(category)) {
				insertProduct(db, product, (int) categoryId);
			}
		}
	}

	private Map<Category, List<Product>> getInitialCategories2Products() {
		Map<Category, List<Product>> res = new LinkedHashMap<Category, List<Product>>();

		String[] initialCategories = context.getResources().getStringArray(R.array.initial_categories);
		Map<String, Category> categoriesTemp = new HashMap<String, Category>();
		for (int i = 0; i < initialCategories.length; i++) {
			String[] p = initialCategories[i].split(":");
			String tempCatId = p[0];
			String categoryName = p[1];
			Category c = new Category();
			c.setName(categoryName);
			c.setOrder(String.valueOf((char) ('a' + i)));
			categoriesTemp.put(tempCatId, c);
			res.put(c, new LinkedList<Product>());
		}
		String[] initialProducts = context.getResources().getStringArray(R.array.initial_products);
		for (int i = 0; i < initialProducts.length; i++) {
			String[] p = initialProducts[i].split(":");
			String tempCatId = p[0];
			String productName = p[1];
			Product prd = new Product();
			prd.setName(productName);
			Category c = categoriesTemp.get(tempCatId);
			if (c == null) {
				throw new RuntimeException("A product with tempCatId = " + tempCatId
						+ " encountered. No category found");
			}
			res.get(c).add(prd);
		}
		return res;
	}

	private long insertCategory(SQLiteDatabase db, Category category) {
		ContentValues v = new ContentValues();
		v.put(CatalogContract.Category.NAME, category.getName());
		v.put(CatalogContract.Category.SORT_ORDER, category.getOrder());
		return db.insert(CatalogContract.Category.TABLE_NAME, null, v);
	}

	private long insertProduct(SQLiteDatabase db, Product product, int categoryId) {
		ContentValues v = new ContentValues();
		v.put(CatalogContract.Product.NAME, product.getName());
		v.put(CatalogContract.Product.CATEGORY_ID, categoryId);
		return db.insert(CatalogContract.Product.TABLE_NAME, null, v);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
