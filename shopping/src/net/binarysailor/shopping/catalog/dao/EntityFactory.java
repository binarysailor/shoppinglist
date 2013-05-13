package net.binarysailor.shopping.catalog.dao;

import net.binarysailor.shopping.catalog.model.Category;
import net.binarysailor.shopping.catalog.model.Product;
import android.database.Cursor;

class EntityFactory {
	static Category createCategory(Cursor cursor) {
		Category c = new Category();
		c.setId(cursor.getInt(0));
		c.setName(cursor.getString(1));
		return c;
	}

	public static Product createProduct(Cursor cursor) {
		Product p = new Product();
		p.setId(cursor.getInt(0));
		p.setName(cursor.getString(1));
		Category c = new Category();
		c.setId(cursor.getInt(2));
		p.setCategory(c);
		return p;
	}
}
