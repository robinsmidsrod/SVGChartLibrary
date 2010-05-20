package no.smidsrod.robin.svg.library;

/**
 * A value represents information about a specific item in all the dimensions
 * available.
 * <p>
 * If the chart has three dimensions, a value must hold one double value for
 * each of the three dimensions.
 * 
 * @author Robin Smidsrød <robin@smidsrod.no>
 * 
 */
public class Value {

	private double[] values;

	/**
	 * @param dimensionCount
	 *            Specify the amount of dimensions this Value should be able to
	 *            support.
	 */
	public Value(int dimensionCount) {
		values = new double[dimensionCount];
	}

	/**
	 * Technically not needed, but provided for convenience.
	 * 
	 * @param chart
	 *            Specify the chart this Value is supposed to be attached to,
	 *            and it will initialize how many dimensions it can support from
	 *            the Chart's class definition.
	 */
	public Value(Chart chart) {
		if (chart == null) {
			throw new NullPointerException(
					"Please specify a valid instance of the Chart interface");
		}
		values = new double[chart.getDimensionCount()];
	}

	/**
	 * @param dimension
	 *            Specify the dimension you want the value for.
	 * @return The value stored for that specific dimension.
	 */
	public double get(int dimension) {
		return values[dimension];
	}

	/**
	 * @param dimension
	 *            Specify the dimension you want to set the value for.
	 * @param value
	 *            The actual value you need to set.
	 */
	public void set(int dimension, double value) {
		values[dimension] = value;
	}

	/**
	 * Technically not required, but provided for convenience.
	 * 
	 * @param range
	 *            Will fetch the dimension from the Range object specified.
	 * @return The value stored for the dimension of the Range object.
	 */
	public double get(Range range) {
		return values[getDimension(range)];
	}

	/**
	 * Technically not required, but provided for convenience.
	 * 
	 * @param range
	 *            Will fetch the dimension from the Range object specified.
	 * @param value
	 *            The actual value you want to store.
	 */
	public void set(Range range, double value) {
		values[getDimension(range)] = value;
	}

	/**
	 * @param range
	 *            The Range object associated with this dimension.
	 * @return The dimension of this Range.
	 */
	private int getDimension(Range range) {
		if (range == null) {
			throw new NullPointerException("Please specify a Range object");
		}
		return range.getDimension();
	}

}
