package net.binarysailor.shopping.shoppinglist;

import java.io.Serializable;

interface SavedShoppingListCommand extends Serializable {
	void execute(ShoppingListEditActivity target);

}
