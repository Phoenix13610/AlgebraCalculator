package algebra;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author AntoineChevalier
 * @version 1.0
 */
public final class MathAC {
	/**
	 * maps a number between a max and min to another max and min
	 * @param number
	 * @param min
	 * @param max
	 * @param min2
	 * @param max2
	 */
	public static double map(double number, double min, double max, double min2, double max2) {
		number -= min;
		max -= min;
		max2 -= min2;
		return number * max2 / max;
	}

	/**
	 * maps a number between a max and 0 to another max and 0
	 * @param number
	 * @param max
	 * @param max2
	 */
	public static double map(double number, double max, double max2) {
		return map(number, 0, max, 0, max2);
	}

	/**
	 * returns true if the String is a double
	 * @param num
	 */
	public static boolean isNum(String num) {
		boolean result = true;
		try {
			if (num.contains("d")) {
				return false;
			}
			Double.parseDouble(num);
		} catch (Exception e) {
			result = false;
		}
		return result;
	}
	
	/**
	 * returns true if the char is a double
	 * @param num
	 */
	public static boolean isNum(char num) {
		String temp = Character.toString(num);
		return isNum(temp);
	}
	
	/**
	 * Transforms a double to a fractionlist containing two values the first being the numerator, the second being the denominator
	 * @param value
	 * @return a list containing two values the first being the numerator, the second being the denominator
	 */
	public static List<Integer> doubleToFraction(double value) {
		List<Integer> result = new ArrayList<Integer>(2);
		int num = toScientificNotation(value).get(0).intValue();
		int denum = (int) Math.pow(10, toScientificNotation(value).get(1).doubleValue());
		double gcd = gcd(num, denum);
		num /= gcd;
		denum /= gcd;
		result.add(num);
		result.add(denum);
		return result;
	}

	/**
	 * Transforms a double to scientific notation 
	 * @param value
	 * @return a list containing two values, the first being the multiplication, the second being the power of 10
	 */
	public static List<Double> toScientificNotation(double value) {
		String[] str = Double.toString(value).split("\\.");
		List<Double> result = new ArrayList<Double>();
		if (str[1].equals("0")) {
			result.add(value);
			result.add(0.0);
			return result;
		}
		value = Double.parseDouble(str[0] + str[1]);
		result.add(value);
		result.add((double) str[1].length());
		return result;
	}

	/**
	 * Find the greatest common divider between two double values
	 * @param a the first double
	 * @param b the second double
	 */
	public static double gcd(double a, double b) {
		if (a < b) {
			return gcd(b, a);
		}
		if (Math.abs(b) < 0.001) {
			return a;
		} else {
			return gcd(b, a - Math.floor(a / b) * b);
		}
	}

	public static int gcd(int a, int b) {
		BigInteger a1 = BigInteger.valueOf(a);
		BigInteger b1 = BigInteger.valueOf(b);
		BigInteger gcd = a1.gcd(b1);
		return gcd.intValue();
	}

	/**
	 * returns false if the double has any decimals
	 * @param d
	 * @return
	 */
	public static boolean isInteger(double d) {
		return d == Math.abs(d);
	}
}
