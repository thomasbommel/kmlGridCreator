package main.test.kmlGridCreator.utils;

import org.junit.Test;
import org.testng.Assert;

import main.java.kmlGridCreator.utils.MathUtils;

public class MathUtilsTest {

	@Test
	public void testNullValues() {
		Assert.assertTrue(MathUtils.equals(null, null));
	}

	@Test
	public void testFirstNull() {
		Assert.assertFalse(MathUtils.equals(null, 1.0));
	}

	@Test
	public void testSecondNull() {
		Assert.assertFalse(MathUtils.equals(null, 1.0));
	}

	@Test
	public void testNormalValues() {
		Assert.assertTrue(MathUtils.equals(1.0, 1.0));
		Assert.assertTrue(MathUtils.equals(Double.MAX_VALUE, Double.MAX_VALUE));
		Assert.assertTrue(MathUtils.equals(Double.MIN_VALUE, Double.MIN_VALUE));
		Assert.assertFalse(MathUtils.equals(1, 2));
		Assert.assertFalse(MathUtils.equals(-39212131, 39212131));
		Assert.assertTrue(MathUtils.equals(1, 1.0));
		Assert.assertTrue(MathUtils.equals(1.0, 1));
		Assert.assertFalse(MathUtils.equals(1, 1.1));
		Assert.assertFalse(MathUtils.equals(1.1, 1));
	}

	@Test
	public void testSmallDifferencesValues() {
		Assert.assertFalse(MathUtils.equals(42.000000001, 42.000000002));
		Assert.assertTrue(MathUtils.equals(42.000000001, 42.000000001));
	}

}
