package net.binarysailor.shopping.catalog.dao;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.binarysailor.shopping.R;
import net.binarysailor.shopping.catalog.model.Category;
import net.binarysailor.shopping.catalog.model.Product;
import net.binarysailor.shopping.common.ModuleSQLiteHelper;
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
				+ "category_id INTEGER REFERENCES category(id) ON DELETE SET NULL, sort_order TEXT, active BOOLEAN)");
		List<Category> categoriesWithProducts = getInitialCategoriesWithProducts();
		for (Category category : categoriesWithProducts) {
			long categoryId = insertCategory(db, category);
			category.setId((int) categoryId);
			for (Product product : category.getProducts()) {
				insertProduct(db, product);
			}
		}
	}

	private List<Category> getInitialCategoriesWithProducts() {
		List<Category> res = new LinkedList<Category>();

		String[] initialCategories = context.getResources().getStringArray(R.array.initial_categories);
		Map<String, Category> categoriesByTempId = new HashMap<String, Category>();
		for (int i = 0; i < initialCategories.length; i++) {
			String[] p = initialCategories[i].split(":");
			String tempCatId = p[0];
			String categoryName = p[1];
			Category c = new Category();
			c.setName(categoryName);
			c.setOrder(String.valueOf((char) ('a' + i)));
			categoriesByTempId.put(tempCatId, c);
			res.add(c);
		}
		String[] initialProducts = context.getResources().getStringArray(R.array.initial_products);
		for (int i = 0; i < initialProducts.length; i++) {
			String[] p = initialProducts[i].split(":");
			String tempCatId = p[0];
			String productName = p[1];
			Product prd = new Product();
			prd.setName(productName);
			Category c = categoriesByTempId.get(tempCatId);
			if (c == null) {
				throw new RuntimeException("A product with tempCatId = " + tempCatId + " encountered. No category found");
			}
			c.addProduct(prd);
		}
		return res;
	}

	private int insertCategory(SQLiteDatabase db, Category category) {
		return (Integer) (new CatalogDatabaseOperations.InsertCategory(category).execute(db));
	}

	private int insertProduct(SQLiteDatabase db, Product product) {
		return (Integer) (new CatalogDatabaseOperations.InsertProduct(product).execute(db));
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
