package net.binarysailor.shopping.shoppinglist.dao;

public class ShoppingListContract {

	static class ShoppingList {
		static final String TABLE_NAME = "shopping_list";
		static final String ID = "id";
		static final String NAME = "name";
	}

	static class EnlistedProduct {
		static final String TABLE_NAME = "enlisted_product";
		static final String ID = "id";
		static final String SHOPPING_LIST_ID = "shopping_list_id";
		static final String PRODUCT_ID = "product_id";
	}
}