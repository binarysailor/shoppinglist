package net.binarysailor.shopping.shoppinglist;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.binarysailor.shopping.R;
import net.binarysailor.shopping.shoppinglist.dao.ShoppingListDAO;
import net.binarysailor.shopping.shoppinglist.model.ShoppingList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ShoppingListsActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping_lists);
		drawShoppingLists();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_shopping_lists, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		ContextMenuInfo menuInfo = item.getMenuInfo();
		View targetView = ((AdapterContextMenuInfo) menuInfo).targetView;
		ShoppingList shoppingList = (ShoppingList) targetView.getTag();
		SavedShoppingListCommand cmd = null;
		switch (item.getItemId()) {
		case R.id.load_list:
		case R.id.merge_list:
			cmd = new SavedShoppingListLoadCommand((ShoppingList) shoppingList.clone(), item.getItemId() == R.id.load_list);
			break;
		case R.id.delete_list:
			new ShoppingListDAO(this).deleteShoppingList(shoppingList.getId());
			cmd = new SavedShoppingListDeleteCommand(shoppingList.getId());
			break;
		}
		Intent intent = new Intent();
		intent.putExtra("command", cmd);
		setResult(RESULT_OK, intent);
		finish();
		return true;
	}

	private void drawShoppingLists() {
		List<ShoppingList> shoppingLists = new ShoppingListDAO(this).getShoppingLists();
		List<Map<String, ?>> data = new LinkedList<Map<String, ?>>();
		for (ShoppingList list : shoppingLists) {
			Map<String, ShoppingList> m = new HashMap<String, ShoppingList>();
			m.put("listObject", list);
			data.add(m);
		}
		SimpleAdapter adapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_1, new String[] { "listObject" },
				new int[] { android.R.id.text1 });
		ListView lv = (ListView) findViewById(R.id.shopping_list_list);
		adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
			@Override
			public boolean setViewValue(View view, Object data, String textRepresentation) {
				ShoppingList list = (ShoppingList) data;
				((TextView) view).setText(list.getName() != null ? list.getName() : "?");
				view.setTag(list);
				return true;
			}
		});
		lv.setAdapter(adapter);
		registerForContextMenu(lv);
	}
}
