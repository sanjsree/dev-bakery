package com.assessment.solution.bakery.app;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import com.assessment.solution.bakery.app.exception.InvalidProductException;

public class BakeryRunner {

	static BakeryUtil util = new BakeryUtil();

	public static void main(String[] args) {
		Scanner sc = null;
		try {
			sc = createOrder(util);
			displayOrderDetails();
			sc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param util
	 * @return Scanner
	 */

	public static Scanner createOrder(BakeryUtil util) {

		Scanner sc = new Scanner(System.in);
		sc.useLocale(Locale.US);
		String s = "";
		int i = 0;

		try {
			System.out.println("We have 3 items available:");
			System.out.println("----------------------------");
			System.out.println("1.Vegemite Scroll(VS5) | 2.Blueberry Muffin(MB11) | 3.Croissant(CF)\n");
			for (int k = 0; k < 3; k++) {
				System.out.println("Enter quantity:");
				i = sc.nextInt();
				System.out.println("Enter product code:");
				s = sc.next();
				util.createOrder(i, s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sc;
	}

	/**
	 * This is a method call for displaying product-wise order details with break-up
	 */
	public static void displayOrderDetails() throws InvalidProductException {
		List<Order> orders = util.getOrders();
		System.out.println("\nOrder details:");
		System.out.println("---------------------------");
		for (Order order : orders) {
			System.out.println("Quantity: " + order.getQuantity() + " | Item: " + order.getItemCode());
			System.out.println("Order breakup details:");
			System.out.println("----------------------");
			System.out.println(util.getBreakUp(order));
			System.out.println("----------------------------------------------------------------");
		}
	}

}
