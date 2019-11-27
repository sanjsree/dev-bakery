package com.assessment.solution.bakery.app;

import java.util.List;

import com.assessment.solution.bakery.app.exception.InsufficientQuantityException;
import com.assessment.solution.bakery.app.exception.InvalidProductException;

public interface Bakery {

	Order createOrder(int quantity, String itemCode);

	List<Order> getOrders();

	String getBreakUp(Order order) throws InsufficientQuantityException, InvalidProductException;

}
