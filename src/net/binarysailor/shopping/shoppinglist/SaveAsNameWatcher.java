package net.binarysailor.shopping.shoppinglist;

import android.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;

public class SaveAsNameWatcher implements TextWatcher {
	private AlertDialog dialog;

	public void setDialog(AlertDialog dialog) {
		this.dialog = dialog;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void afterTextChanged(Editable s) {
		String listName = s.toString();
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(isValid(listName));
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	private boolean isValid(String listName) {
		return !listName.trim().equals("");
	}

}
