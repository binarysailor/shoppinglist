package net.binarysailor.shopping.catalog;

import android.content.DialogInterface;

public class DeleteCancelClickListener implements DialogInterface.OnClickListener {
	@Override
	public void onClick(DialogInterface dialog, int which) {
		dialog.cancel();
	}
}
