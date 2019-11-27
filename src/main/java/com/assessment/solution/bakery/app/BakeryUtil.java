package com.assessment.solution.bakery.app;

//import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.assessment.solution.bakery.app.exception.InsufficientQuantityException;
import com.assessment.solution.bakery.app.exception.InvalidProductException;

public class BakeryUtil implements Bakery {

	static Properties p;

	// product codes
	static String VS5_CODE, MB11_CODE, CF_CODE;

	// product bunches
	static int VS5_3_UNITS, VS5_5_UNITS;
	static int MB11_2_UNITS, MB11_5_UNITS, MB11_8_UNITS;
	static int CF_3_UNITS, CF_5_UNITS, CF_9_UNITS;

	// per bunch costs
	static double VS5_3_UNIT_COST, VS5_5_UNIT_COST;
	static double MB11_2_UNIT_COST, MB11_5_UNIT_COST, MB11_8_UNIT_COST;
	static double CF_3_UNIT_COST, CF_5_UNIT_COST, CF_9_UNIT_COST;

	static {
		try {
			loadProperties();
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public static void loadProperties() throws NumberFormatException, IOException {

		p = new Properties();
		p.load(BakeryUtil.class.getClassLoader().getResourceAsStream("cost-breakup.properties"));

		VS5_CODE = p.getProperty("vs5.itemcode");
		MB11_CODE = p.getProperty("mb11.itemcode");
		CF_CODE = p.getProperty("cf.itemcode");

		VS5_3_UNITS = Integer.parseInt(p.getProperty("vs5.3.units"));
		VS5_5_UNITS = Integer.parseInt(p.getProperty("vs5.5.units"));
		MB11_2_UNITS = Integer.parseInt(p.getProperty("mb11.2.units"));
		MB11_5_UNITS = Integer.parseInt(p.getProperty("mb11.5.units"));
		MB11_8_UNITS = Integer.parseInt(p.getProperty("mb11.8.units"));
		CF_3_UNITS = Integer.parseInt(p.getProperty("cf.3.units"));
		CF_5_UNITS = Integer.parseInt(p.getProperty("cf.5.units"));
		CF_9_UNITS = Integer.parseInt(p.getProperty("cf.9.units"));

		VS5_3_UNIT_COST = Double.parseDouble(p.getProperty("vs5.3.unitcost"));
		VS5_5_UNIT_COST = Double.parseDouble(p.getProperty("vs5.5.unitcost"));
		MB11_2_UNIT_COST = Double.parseDouble(p.getProperty("mb11.2.unitcost"));
		MB11_5_UNIT_COST = Double.parseDouble(p.getProperty("mb11.5.unitcost"));
		MB11_8_UNIT_COST = Double.parseDouble(p.getProperty("mb11.8.unitcost"));
		CF_3_UNIT_COST = Double.parseDouble(p.getProperty("cf.3.unitcost"));
		CF_5_UNIT_COST = Double.parseDouble(p.getProperty("cf.5.unitcost"));
		CF_9_UNIT_COST = Double.parseDouble(p.getProperty("cf.9.unitcost"));

		// fr.close();
	}

	private Order order;
	private List<Order> orders = new ArrayList<>();
	private NumberFormat nf;

	/**
	 * This method calculates cost breakup in scenarios where cost calculation for
	 * combination of bunches is involved - depending on product code
	 * 
	 * @param order
	 * @param costMap
	 * @param smallerUnits
	 */
	public void calculateComplexCost(Order order, Map<Double, Integer> costMap, int smallerUnits, int smallBunch,
			int largeBunch, double smallBunchCost, double largeBunchCost) {

		int numOflargerUnits = 0, numOfSmallerUnits = 0;
		double largerCost = 0.0d, smallerCost = 0.0d, totalCost = 0.0d;

		numOfSmallerUnits = smallerUnits / smallBunch;
		smallerCost = numOfSmallerUnits * smallBunchCost;
		numOflargerUnits = order.getQuantity() / largeBunch;
		largerCost = numOflargerUnits * largeBunchCost;
		totalCost = smallerCost + largerCost;

		nf = getNumberFormat();
		System.out.println("total cost for " + order.getQuantity() + " units is $" + nf.format(totalCost));

		if (numOflargerUnits > 0) {
			costMap.put(largeBunchCost, numOflargerUnits);
		}
		if (numOfSmallerUnits > 0) {
			costMap.put(smallBunchCost, numOfSmallerUnits);
		}

	}

	/**
	 * This method calculates cost breakup in bunches of 3,5,9 or combination of all
	 * - EXCLUSIVELY FOR CF product
	 * 
	 * @param order
	 * @param costMap
	 * @throws NumberFormatException
	 * @throws InsufficientQuantityException
	 */
	public void calculateCostBreakUpForCF(Order order, Map<Double, Integer> costMap)
			throws InsufficientQuantityException {

		int smallerUnits = 0;

		try {
			if (order.getQuantity() < CF_3_UNITS) {
				throw new InsufficientQuantityException("Please place an order for atleast 3 items");
			}

			if (order.getQuantity() >= CF_9_UNITS) {
				if (order.getQuantity() % CF_9_UNITS == 0) {
					calculateSimpleCost(order, costMap, CF_9_UNITS, CF_9_UNIT_COST);
				} else if (order.getQuantity() % CF_9_UNITS > 0) {
					smallerUnits = order.getQuantity() % CF_9_UNITS;
					if (smallerUnits % CF_3_UNITS == 0) {
						calculateComplexCost(order, costMap, smallerUnits, CF_3_UNITS, CF_9_UNITS, CF_3_UNIT_COST,
								CF_9_UNIT_COST);
					} else if (smallerUnits % CF_5_UNITS == 0) {
						calculateComplexCost(order, costMap, smallerUnits, CF_5_UNITS, CF_9_UNITS, CF_5_UNIT_COST,
								CF_9_UNIT_COST);
					} else {
						calculateComplexCost(order, costMap, smallerUnits, CF_3_UNITS, CF_5_UNITS, CF_3_UNIT_COST,
								CF_5_UNIT_COST);
					}
				}
			} else if (order.getQuantity() < CF_9_UNITS) {
				if (order.getQuantity() % CF_3_UNITS == 0) {
					calculateSimpleCost(order, costMap, CF_3_UNITS, CF_3_UNIT_COST);
				} else if (order.getQuantity() % CF_5_UNITS == 0) {
					calculateSimpleCost(order, costMap, CF_5_UNITS, CF_5_UNIT_COST);
				} else if (order.getQuantity() % CF_5_UNITS > 0) {
					smallerUnits = order.getQuantity() % CF_5_UNITS;
					if (smallerUnits % CF_3_UNITS == 0) {
						calculateComplexCost(order, costMap, smallerUnits, CF_3_UNITS, CF_5_UNITS, CF_3_UNIT_COST,
								CF_5_UNIT_COST);
					}
				}
			} else {
				throw new InsufficientQuantityException(
						"This product is sold in bunches of 3,5 or 9 only!! Please enter a valid quantity!!");
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method calculates cost breakup in bunches of 2,5,8 or combination of all
	 * - EXCLUSIVELY FOR MB11 product
	 * 
	 * @param order
	 * @param costMap
	 * @throws NumberFormatException
	 * @throws InsufficientQuantityException
	 */
	public void calculateCostBreakUpForMB11(Order order, Map<Double, Integer> costMap)
			throws InsufficientQuantityException {

		int smallerUnits = 0;

		try {
			if (order.getQuantity() < MB11_2_UNITS) {
				throw new InsufficientQuantityException("Please place an order for atleast 2 items");
			}

			if (order.getQuantity() >= MB11_8_UNITS) {
				if (order.getQuantity() % MB11_8_UNITS == 0) {
					calculateSimpleCost(order, costMap, MB11_8_UNITS, MB11_8_UNIT_COST);
				} else if (order.getQuantity() % MB11_8_UNITS > 0) {
					smallerUnits = order.getQuantity() % MB11_8_UNITS;
					if (smallerUnits % MB11_5_UNITS == 0) {
						calculateComplexCost(order, costMap, smallerUnits, MB11_5_UNITS, MB11_8_UNITS, MB11_5_UNIT_COST,
								MB11_8_UNIT_COST);
					} else if (smallerUnits % MB11_2_UNITS == 0) {
						calculateComplexCost(order, costMap, smallerUnits, MB11_2_UNITS, MB11_8_UNITS, MB11_2_UNIT_COST,
								MB11_8_UNIT_COST);
					} else {
						calculateComplexCost(order, costMap, smallerUnits, MB11_2_UNITS, MB11_5_UNITS, MB11_2_UNIT_COST,
								MB11_5_UNIT_COST);
					}
				}
			} else if (order.getQuantity() >= MB11_2_UNITS && order.getQuantity() <= MB11_5_UNITS) {
				if (order.getQuantity() % MB11_2_UNITS == 0) {
					calculateSimpleCost(order, costMap, MB11_2_UNITS, MB11_2_UNIT_COST);
				} else if (order.getQuantity() % MB11_5_UNITS == 0) {
					calculateSimpleCost(order, costMap, MB11_5_UNITS, MB11_5_UNIT_COST);
				} else {
					calculateComplexCost(order, costMap, smallerUnits, MB11_2_UNITS, MB11_5_UNITS, MB11_2_UNIT_COST,
							MB11_5_UNIT_COST);
				}
			} else {
				throw new InsufficientQuantityException(
						"This product is sold in bunches of 2,5 or 8 only!! Please enter a valid quantity!!");
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method calculates cost breakup in bunches of 3,5 or combination of both
	 * - EXCLUSIVELY FOR VS5 product
	 * 
	 * @param order
	 * @param costMap
	 * @throws NumberFormatException
	 * @throws InsufficientQuantityException
	 */
	public void calculateCostBreakUpForVS5(Order order, Map<Double, Integer> costMap)
			throws InsufficientQuantityException {

		int smallerUnits = 0;

		if (order.getQuantity() < VS5_3_UNITS) {
			throw new InsufficientQuantityException("Please place an order for atleast 3 items");
		}

		if (order.getQuantity() % VS5_3_UNITS == 0) {
			calculateSimpleCost(order, costMap, VS5_3_UNITS, VS5_3_UNIT_COST);
		}

		if (order.getQuantity() >= MB11_5_UNITS) {
			if (order.getQuantity() % VS5_5_UNITS == 0) {
				calculateSimpleCost(order, costMap, VS5_5_UNITS, VS5_5_UNIT_COST);
			} else if (order.getQuantity() % VS5_5_UNITS > 0) {
				smallerUnits = order.getQuantity() % VS5_5_UNITS;
				if (smallerUnits % VS5_3_UNITS == 0) {
					calculateComplexCost(order, costMap, smallerUnits, VS5_3_UNITS, VS5_5_UNITS, VS5_3_UNIT_COST,
							VS5_5_UNIT_COST);
				} else {
					throw new InsufficientQuantityException(
							"This product is sold in bunches of 3 or 5 only!! Please enter a valid quantity!!");
				}
			}
		}
	}

	/**
	 * This method calculates cost breakup in multiples of bunches of 2,3,5,8 or 9 -
	 * depending on product code
	 * 
	 * @param order
	 * @param costMap
	 * @param unitBunch
	 * @param unitBunchCost
	 */
	public void calculateSimpleCost(Order order, Map<Double, Integer> costMap, int bunch, double bunchCost) {

		int units = 0;
		double totalCost = 0.0d;

		try {
			units = order.getQuantity() / bunch;
			totalCost = units * bunchCost;

			nf = getNumberFormat();
			System.out.println("total cost for " + order.getQuantity() + " units is $" + nf.format(totalCost));
			costMap.put(bunchCost, units);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Order createOrder(int quantity, String itemCode) {
		order = new Order(quantity, itemCode);
		orders.add(order);
		return order;
	}

	/**
	 * This method returns a string result containing cost breakup details for each
	 * product sorted in the reverse order
	 * 
	 * @return String
	 * @throws InvalidProductException
	 */
	@Override
	public String getBreakUp(Order order) throws InvalidProductException {

		Map<Double, Integer> costMap = new LinkedHashMap<>();
		LinkedHashMap<Double, Integer> reverseSortedMap = new LinkedHashMap<>();
		String costBreakUpDetails = "";

		boolean validItemCode = isValidItemCode(order.getItemCode());

		if (validItemCode) {
			costMap = getOrderBreakUp(order, costMap);
			costMap.entrySet().stream().sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
					.forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));
		} else {
			throw new InvalidProductException("Please enter a valid product code!!");
		}
		for (Entry<Double, Integer> entry : reverseSortedMap.entrySet()) {
			costBreakUpDetails = costBreakUpDetails + "packet(s): " + entry.getValue() + ", cost/packet: $"
					+ entry.getKey() + " | ";
		}

		return costBreakUpDetails;
	}

	/**
	 * This method returns a formatted double result up to 2 decimal digits
	 * precision
	 * 
	 * @return NumberFormat
	 */
	public NumberFormat getNumberFormat() {
		NumberFormat nf = NumberFormat.getInstance(new Locale("en", "US"));
		nf.setMaximumFractionDigits(2);
		return nf;
	}

	public Map<Double, Integer> getOrderBreakUp(Order order, Map<Double, Integer> costMap) {

		try {
			switch (order.getItemCode()) {
			case "VS5":
				calculateCostBreakUpForVS5(order, costMap);
				break;
			case "MB11":
				calculateCostBreakUpForMB11(order, costMap);
				break;
			case "CF":
				calculateCostBreakUpForCF(order, costMap);
				break;
			default:
				System.out.println("Invalid Product Code!!");
				break;
			}
		} catch (NumberFormatException | InsufficientQuantityException e) {
			e.printStackTrace();
		}
		return costMap;
	}

	@Override
	public List<Order> getOrders() {
		return orders;
	}

	public boolean isValidItemCode(String itemCode) {
		boolean valid = true;
		if (!((itemCode.equalsIgnoreCase(VS5_CODE)) || (itemCode.equalsIgnoreCase(MB11_CODE))
				|| (itemCode.equalsIgnoreCase(CF_CODE)))) {
			valid = false;
		}
		return valid;
	}
}