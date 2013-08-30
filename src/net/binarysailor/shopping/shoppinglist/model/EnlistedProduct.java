package net.binarysailor.shopping.shoppinglist.model;

import java.io.Serializable;
import java.math.BigDecimal;

import net.binarysailor.shopping.catalog.model.Product;

public class EnlistedProduct implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	private Product product;
	private String nonCatalogProductName;
	private BigDecimal quantity;

	public EnlistedProduct(Product product, BigDecimal quantity) {
		this.product = product;
		this.quantity = quantity;
	}

	public EnlistedProduct(String name, BigDecimal quantity) {
		this.nonCatalogProductName = name;
		this.quantity = quantity;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public void setNonCatalogProductName(String nonCatalogProductName) {
		this.nonCatalogProductName = nonCatalogProductName;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	@Override
	protected Object clone() {
		return new EnlistedProduct(product, quantity);
	}

}
