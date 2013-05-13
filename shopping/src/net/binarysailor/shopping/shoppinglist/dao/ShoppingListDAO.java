package net.binarysailor.shopping.shoppinglist.dao;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import net.binarysailor.shopping.catalog.model.Product;
import net.binarysailor.shopping.common.DatabaseUtils;
import net.binarysailor.shopping.shoppinglist.model.ShoppingList;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ShoppingListDAO {

	private static List<ShoppingList> shoppingLists;

	private Context context;

	public ShoppingListDAO(Context context) {
		this.context = context;
	}

	public List<ShoppingList> getShoppingLists() {
		List<ShoppingList> sl = ShoppingListDAO.shoppingLists;
		if (sl == null) {
			sl = loadShoppingLists();
			ShoppingListDAO.shoppingLists = sl;
		}
		return sl;
	}

	private List<ShoppingList> loadShoppingLists() {
		List<ShoppingList> shoppingLists = new LinkedList<ShoppingList>();
		SQLiteDatabase db = DatabaseUtils.getReadableDatabase(context);
		Cursor cursor = db.rawQuery("SELECT sl.id, sl.name, ep.quantity, p.id, p.name " + "FROM shopping_list sl "
				+ "JOIN enlisted_product ep ON sl.id = ep.shopping_list_id "
				+ "JOIN product p ON p.id = ep.product_id ORDER BY sl.name, sl.id", null);
		boolean dataAvailable = cursor.moveToFirst();
		ShoppingList current = null;
		while (dataAvailable) {
			int shoppingListId = cursor.getInt(0);
			if (current == null || shoppingListId != current.getId()) {
				current = new ShoppingList();
				current.setId(shoppingListId);
				current.setName(cursor.getString(1));
				shoppingLists.add(current);
			}
			Product p = new Product();
			p.setId(cursor.getInt(3));
			p.setName(cursor.getString(4));
			BigDecimal quantity = new BigDecimal(cursor.getDouble(2));
			quantity = quantity.setScale(2, BigDecimal.ROUND_HALF_EVEN);
			current.enlistProduct(p, quantity);
			dataAvailable = cursor.moveToNext();
		}
		cursor.close();
		db.close();
		return shoppingLists;
	}
}
