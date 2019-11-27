package com.assessment.solution.bakery.app;

public class Order {
	
	private int quantity;
	private String itemCode;
	
	public Order(int quantity, String itemCode) {
		this.setQuantity(quantity);
		this.setItemCode(itemCode);
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	
	@Override
	public String toString() {
		return "Order [quantity=" + quantity + ", itemCode=" + itemCode + "]";
	}

}
