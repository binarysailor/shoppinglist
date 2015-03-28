package net.binarysailor.shopping.shoppinglist;

import net.binarysailor.shopping.R;
import net.binarysailor.shopping.shoppinglist.tabs.TabsListener;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class ShoppingListTabbedActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		TabsListener l = new TabsListener();
		actionBar.addTab(actionBar.newTab().setText("Tab 1").setTabListener(l).setText("Prepare"));
		actionBar.addTab(actionBar.newTab().setText("Tab 2").setTabListener(l).setText("Go shopping"));

		setContentView(R.layout.activity_tabbed_shopping);

	}

}
