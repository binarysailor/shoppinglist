package net.binarysailor.shopping.catalog;

import net.binarysailor.shopping.R;
import net.binarysailor.shopping.catalog.dao.CatalogDAO;
import net.binarysailor.shopping.catalog.model.Product;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.Toast;

class ProductDeleteConfirmClickListener implements OnClickListener {
	private CatalogEditActivity activity;
	private Product product;

	public ProductDeleteConfirmClickListener(CatalogEditActivity activity, Product product) {
		this.activity = activity;
		this.product = product;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		new CatalogDAO(activity).deleteProduct(product.getId());
		activity.refreshProductTree();
		Toast.makeText(
				activity,
				String.format(activity.getString(R.string.catalog_edit_deleteProduct_confirmation_toast),
						product.getName()), Toast.LENGTH_SHORT).show();

	}
}
