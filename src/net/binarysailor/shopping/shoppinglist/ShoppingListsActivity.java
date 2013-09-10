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
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ShoppingListsActivity extends Activity {

	private List<Map<String, ?>> shoppingListsAdapterData;
	private SimpleAdapter shoppingListsAdapter;

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
		SavedShoppingListCommand returnCommand = null;
		switch (item.getItemId()) {
		case R.id.load_list:
		case R.id.merge_list:
			returnCommand = new SavedShoppingListLoadCommand((ShoppingList) shoppingList.clone(), item.getItemId() == R.id.load_list);
			break;
		case R.id.delete_list:
			new ShoppingListDAO(this).deleteShoppingList(shoppingList.getId());
			refreshList();
			break;
		}
		if (returnCommand != null) {
			closeAndPassCommand(returnCommand);
		}
		return true;
	}

	private void drawShoppingLists() {
		shoppingListsAdapterData = new LinkedList<Map<String, ?>>();
		shoppingListsAdapter = new SimpleAdapter(this, shoppingListsAdapterData, android.R.layout.simple_list_item_1,
				new String[] { "listObject" }, new int[] { android.R.id.text1 });
		ListView lv = (ListView) findViewById(R.id.shopping_list_list);
		shoppingListsAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
			@Override
			public boolean setViewValue(View view, Object data, String textRepresentation) {
				final ShoppingList list = (ShoppingList) data;
				((TextView) view).setText(list.getName() != null ? list.getName() : "?");
				view.setTag(list);
				return true;
			}
		});
		lv.setAdapter(shoppingListsAdapter);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> av, View view, int arg2, long arg3) {
				final ShoppingList list = (ShoppingList) view.getTag();
				if (list != null) {
					closeAndPassCommand(new SavedShoppingListLoadCommand((ShoppingList) list.clone(), true));
				}
			}
		});
		registerForContextMenu(lv);
		refreshList();
	}

	private void refreshList() {
		shoppingListsAdapterData.clear();
		List<ShoppingList> shoppingLists = new ShoppingListDAO(this).getShoppingLists();
		for (ShoppingList list : shoppingLists) {
			Map<String, ShoppingList> m = new HashMap<String, ShoppingList>();
			m.put("listObject", list);
			shoppingListsAdapterData.add(m);
		}
		shoppingListsAdapter.notifyDataSetChanged();
	}

	private void closeAndPassCommand(SavedShoppingListCommand returnCommand) {
		Intent intent = new Intent();
		intent.putExtra("command", returnCommand);
		setResult(RESULT_OK, intent);
		finish();
	}

}
