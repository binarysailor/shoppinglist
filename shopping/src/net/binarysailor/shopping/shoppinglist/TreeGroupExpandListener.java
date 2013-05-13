package net.binarysailor.shopping.shoppinglist;

import java.util.Set;

import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

class TreeGroupExpandListener implements OnGroupExpandListener, OnGroupCollapseListener {

	Set<Integer> expanded;

	public TreeGroupExpandListener(Set<Integer> expanded) {
		this.expanded = expanded;
	}

	@Override
	public void onGroupCollapse(int groupPosition) {
		expanded.remove(groupPosition);
	}

	@Override
	public void onGroupExpand(int groupPosition) {
		expanded.add(groupPosition);
	}

}
