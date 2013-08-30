package net.binarysailor.shopping.shoppinglist;

import net.binarysailor.shopping.shoppinglist.model.ShoppingList;

public class SavedShoppingListLoadCommand implements SavedShoppingListCommand {

	private static final long serialVersionUID = 1L;

	private ShoppingList shoppingList;
	private boolean replace;

	SavedShoppingListLoadCommand(ShoppingList shoppingList, boolean replace) {
		this.shoppingList = shoppingList;
		this.replace = replace;
	}

	public void execute(ShoppingListEditActivity target) {
		if (replace) {
			target.loadList(shoppingList);
		} else {
			target.getProductSelection().add(shoppingList.toSelection());
		}
	}

}
