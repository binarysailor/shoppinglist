package net.binarysailor.shopping.catalog.dao;

public class CatalogContract {
	static class Category {
		static final String TABLE_NAME = "category";
		static final String ID = "id";
		static final String NAME = "name";
		static final String SORT_ORDER = "sort_order";
		static final String ACTIVE = "active";
	}

	static class Product {
		static final String TABLE_NAME = "product";
		static final String ID = "id";
		static final String NAME = "name";
		static final String CATEGORY_ID = "category_id";
		static final String SORT_ORDER = "sort_order";
	}
}
