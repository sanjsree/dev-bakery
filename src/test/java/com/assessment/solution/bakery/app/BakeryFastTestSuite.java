package com.assessment.solution.bakery.app;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class BakeryFastTestSuite {

	public static void main(String[] args) {

		Result result = JUnitCore.runClasses(BakeryJunitTestSuite.class);

		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}

		System.out.println(result.wasSuccessful());
	}

}
