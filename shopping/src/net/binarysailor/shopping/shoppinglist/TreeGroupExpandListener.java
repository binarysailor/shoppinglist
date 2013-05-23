package net.binarysailor.shopping.shoppinglist;

import java.util.Set;

import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

class TreeGroupExpandListener implements OnGroupExpandListener, OnGroupCollapseListener {

	Set<Integer> expanded;
	boolean listening = true;

	public TreeGroupExpandListener(Set<Integer> expanded) {
		this.expanded = expanded;
	}

	public Set<Integer> getExpanded() {
		return expanded;
	}

	public void setExpanded(Set<Integer> expanded) {
		this.expanded = expanded;
	}

	@Override
	public void onGroupCollapse(int groupPosition) {
		if (listening)
			expanded.remove(groupPosition);
	}

	@Override
	public void onGroupExpand(int groupPosition) {
		if (listening)
			expanded.add(groupPosition);
	}

	public void stopListening() {
		listening = false;
	}

	public void startListening() {
		listening = true;
	}
}
