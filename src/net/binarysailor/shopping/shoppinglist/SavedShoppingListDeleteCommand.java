package net.binarysailor.shopping.shoppinglist;

public class SavedShoppingListDeleteCommand implements SavedShoppingListCommand {

	private static final long serialVersionUID = 1L;

	private int deletedListId;

	public SavedShoppingListDeleteCommand(int id) {
		this.deletedListId = id;
	}

	@Override
	public void execute(ShoppingListEditActivity target) {

	}

}
