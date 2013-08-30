package net.binarysailor.shopping.shoppinglist.dao;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.binarysailor.shopping.catalog.dao.CatalogDAO;
import net.binarysailor.shopping.catalog.model.Product;
import net.binarysailor.shopping.common.DatabaseUtils;
import net.binarysailor.shopping.shoppinglist.model.ProductSelection;
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
		assertShoppingListsLoaded();
		return ShoppingListDAO.shoppingLists;
	}

	public ShoppingList getShoppingListById(int id) {
		List<ShoppingList> lists = getShoppingLists();
		for (ShoppingList list : lists) {
			if (list.getId() == id) {
				return list;
			}
		}
		return null;
	}

	public ShoppingList createShoppingList(ShoppingList list) {
		assertShoppingListsLoaded();
		Integer listId = (Integer) DatabaseUtils.executeInTransaction(context, new ShoppingListDatabaseOperations.InsertShoppingList(list));

		list.setId(listId);
		ShoppingListDAO.shoppingLists.add(list);
		return list;
	}

	public void deleteShoppingList(int id) {
		assertShoppingListsLoaded();
		DatabaseUtils.executeInTransaction(context, new ShoppingListDatabaseOperations.DeleteShoppingList(id));
		for (Iterator<ShoppingList> lists = ShoppingListDAO.shoppingLists.iterator(); lists.hasNext();) {
			ShoppingList list = lists.next();
			if (list.getId() == id) {
				lists.remove();
			}
		}
	}

	public void updateShoppingListContents(int id, ProductSelection selection) {
		ShoppingList toBeUpdated = getShoppingListById(id);
		if (toBeUpdated != null) {
			ShoppingList dto = (ShoppingList) toBeUpdated.clone();
			Collection<Product> products = getProducts(selection);
			dto.setProducts(products);
			DatabaseUtils.executeInTransaction(context, new ShoppingListDatabaseOperations.UpdateShoppingListContents(dto));
			toBeUpdated.setProducts(products);
		}
	}

	private void assertShoppingListsLoaded() {
		if (ShoppingListDAO.shoppingLists == null) {
			loadShoppingLists();
		}
	}

	private void loadShoppingLists() {
		List<ShoppingList> shoppingLists = new LinkedList<ShoppingList>();
		SQLiteDatabase db = DatabaseUtils.getReadableDatabase(context);
		Cursor cursor = db.rawQuery("SELECT sl.id, sl.name, ep.quantity, ep.non_catalog_name, p.id, p.name FROM shopping_list sl "
				+ "JOIN enlisted_product ep ON sl.id = ep.shopping_list_id "
				+ "LEFT JOIN product p ON p.id = ep.product_id ORDER BY sl.name, sl.id", null);
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
			if (cursor.isNull(4)) {
				String name = cursor.getString(3);
				current.enlistNonCatalogProduct(name, BigDecimal.ONE);
			} else {
				Product p = new Product();
				p.setId(cursor.getInt(4));
				p.setName(cursor.getString(5));
				/*
				BigDecimal quantity = new BigDecimal(cursor.getDouble(2));
				quantity = quantity.setScale(2, BigDecimal.ROUND_HALF_EVEN);
				*/
				current.enlistProduct(p, BigDecimal.ONE);
			}
			dataAvailable = cursor.moveToNext();
		}
		cursor.close();
		db.close();
		ShoppingListDAO.shoppingLists = shoppingLists;
	}

	private Collection<Product> getProducts(ProductSelection selection) {
		CatalogDAO catalogDAO = new CatalogDAO(context);
		return catalogDAO.getProducts(selection);
	}
}
