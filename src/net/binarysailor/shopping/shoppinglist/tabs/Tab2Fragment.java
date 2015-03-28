package net.binarysailor.shopping.shoppinglist.tabs;

import net.binarysailor.shopping.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Tab2Fragment extends Fragment {

	private String text;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.tab2_layout, null);
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public void onStart() {
		super.onStart();
		((TextView) getView()).setText(text);
	}
}
