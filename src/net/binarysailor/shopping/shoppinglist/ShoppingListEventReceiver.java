package net.binarysailor.shopping.shoppinglist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ShoppingListEventReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context ctx, Intent intent) {
		FlatShoppingListActivity.clearDoneProducts();
	}

}
