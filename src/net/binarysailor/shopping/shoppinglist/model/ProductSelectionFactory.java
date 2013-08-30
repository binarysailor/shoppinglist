package net.binarysailor.shopping.shoppinglist.model;

import android.content.Intent;

public class ProductSelectionFactory {
	public static ProductSelection getProductSelection(Intent intent) {
		if (intent.getExtras() != null && intent.getExtras().get("selection") != null) {
			return (ProductSelection) intent.getExtras().get("selection");
		} else {
			return new ProductSelection();
		}
	}
}
