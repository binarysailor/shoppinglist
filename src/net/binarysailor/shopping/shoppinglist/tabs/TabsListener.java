package net.binarysailor.shopping.shoppinglist.tabs;

import net.binarysailor.shopping.R;
import net.binarysailor.shopping.shoppinglist.ProductSelectionFragment;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;

public class TabsListener implements TabListener {

	private ProductSelectionFragment fragment1;
	private Tab2Fragment fragment2;

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		if (tab.getPosition() == 0) {
			if (fragment1 != null) {
				ft.attach(fragment1);
			} else {
				fragment1 = new ProductSelectionFragment();
				ft.add(R.id.tabcontainer, fragment1);
			}
		} else {
			if (fragment2 != null) {
				ft.attach(fragment2);
			} else {
				fragment2 = new Tab2Fragment();
				ft.add(R.id.tabcontainer, fragment2);
			}
			fragment2.setText(String.valueOf(fragment1.getNumber()));
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		if (tab.getPosition() == 0) {
			ft.detach(fragment1);
		} else {
			ft.detach(fragment2);
		}
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

}
