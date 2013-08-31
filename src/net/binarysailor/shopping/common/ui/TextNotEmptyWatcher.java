package net.binarysailor.shopping.common.ui;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

class TextNotEmptyWatcher implements TextWatcher {
	private View targetView;

	public void setTargetView(View targetView) {
		this.targetView = targetView;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void afterTextChanged(Editable s) {
		if (targetView != null) {
			String listName = s.toString();
			targetView.setEnabled(isValid(listName));
		}
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	private boolean isValid(String listName) {
		return !listName.trim().equals("");
	}

}
