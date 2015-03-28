package net.binarysailor.shopping.shoppinglist;

import net.binarysailor.shopping.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

public class ProductSelectionFragment extends Fragment {

	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view != null) {
			return view;
		} else {
			view = inflater.inflate(R.layout.tab1_layout, null);
			return view;
		}
		//return super.onCreateView(inflater, container, savedInstanceState);
	}

	public int getNumber() {
		NumberPicker picker = picker();
		return picker.getValue();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			picker().setValue(savedInstanceState.getInt("picker_value"));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("picker_value", picker().getValue());
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		NumberPicker picker = picker();
		picker.setMinValue(0);
		picker.setMaxValue(20);
		super.onActivityCreated(savedInstanceState);
	}

	private NumberPicker picker() {
		return (NumberPicker) getView().findViewById(R.id.np);
	}
}
