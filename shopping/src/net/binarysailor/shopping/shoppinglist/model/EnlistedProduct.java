package net.binarysailor.shopping.shoppinglist.model;

import java.math.BigDecimal;

import net.binarysailor.shopping.catalog.model.Product;

public class EnlistedProduct {
	private Product product;
	private BigDecimal quantity;

	public EnlistedProduct(Product product, BigDecimal quantity) {
		this.product = product;
		this.quantity = quantity;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

}
