package net.binarysailor.shopping.catalog;

import net.binarysailor.shopping.R;
import net.binarysailor.shopping.catalog.dao.CatalogDAO;
import net.binarysailor.shopping.catalog.model.Category;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.Toast;

public class CategoryDeleteConfirmClickListener implements OnClickListener {

	private CatalogEditActivity activity;
	private Category category;

	public CategoryDeleteConfirmClickListener(CatalogEditActivity activity, Category category) {
		this.activity = activity;
		this.category = category;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		new CatalogDAO(activity).deleteCategory(category.getId());
		activity.refreshProductTree();
		Toast.makeText(
				activity,
				String.format(activity.getString(R.string.catalog_edit_deleteCategory_confirmation_toast),
						category.getName()), Toast.LENGTH_SHORT).show();
	}

}
