package com.assessment.solution.bakery.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.assessment.solution.bakery.app.exception.InsufficientQuantityException;
import com.assessment.solution.bakery.app.exception.InvalidProductException;

public class BakeryUtilTest {

	static BakeryUtil util;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		util = new BakeryUtil();
	}

	
	@Test(expected = FileNotFoundException.class)
	public void readPropertiesFNFExceptionTest() throws IOException {
		FileReader fr = new FileReader("cost-breakup.properties");
		fr.read();
		fr.close();
	}
	 

	@Test
	public void isCFValidItemCodeTest() {
		assertTrue(util.isValidItemCode("CF"));
	}

	@Test
	public void isVS5ValidItemCodeTest() {
		assertTrue(util.isValidItemCode("VS5"));
	}

	@Test
	public void isMB11ValidItemCodeTest() {
		assertTrue(util.isValidItemCode("MB11"));
	}

	@Test
	public void inValidItemCodeTest() {
		assertFalse(util.isValidItemCode("ABC"));
	}

	@Test
	public void createOrderTest() {
		Order order = util.createOrder(10, "VS5");
		assertEquals(10, order.getQuantity());
		assertEquals("VS5", order.getItemCode());
	}

	@Test
	public void getBreakUpTest() throws com.assessment.solution.bakery.app.exception.InvalidProductException {
		Order order = new Order(10, "VS5");
		Map<Double, Integer> testMap = new HashMap<>();
		testMap .put(8.99, 2);
		String breakUp = "packet(s): " + testMap.get(8.99) + ", cost/packet: $" + 8.99 + " | ";
		assertEquals(breakUp, util.getBreakUp(order));
	}

	@Test(expected = NullPointerException.class)
	public void getOrderBreakUpNullPointerTest() {
		Order order = new Order(5, null);
		Map<Double, Integer> testMap = new HashMap<>();
		util.getOrderBreakUp(order, testMap);
	}

	@Test(expected = InvalidProductException.class)
	public void getBreakUpInvalidProductExceptionTest() throws InvalidProductException {
		Order order = new Order(5, "ABC");
		util.getBreakUp(order);
	}

	@Test(expected = InsufficientQuantityException.class)
	public void calculateCostBreakUpForCFExceptionTest() throws InsufficientQuantityException {
		Order order = new Order(2, "CF");
		util.calculateCostBreakUpForCF(order, null);
	}

	@Test(expected = InsufficientQuantityException.class)
	public void calculateCostBreakUpForMB11ExceptionTest() throws InsufficientQuantityException {
		Order order = new Order(1, "MB11");
		util.calculateCostBreakUpForMB11(order, null);
	}

	@Test(expected = InsufficientQuantityException.class)
	public void calculateCostBreakUpForVS5ExceptionTest() throws InsufficientQuantityException {
		Order order = new Order(2, "VS5");
		util.calculateCostBreakUpForVS5(order, null);
	}
	
	@Test
	public void getNumberFormatTest() {
		NumberFormat nf = NumberFormat.getInstance(new Locale("en", "US"));
		nf.setMaximumFractionDigits(2);
		assertEquals(nf, util.getNumberFormat());
	}
	

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		util = null;
	}

}
