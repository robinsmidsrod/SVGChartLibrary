package no.smidsrod.robin.svg.library;

import java.util.List;

/**
 * @author Robin Smidsrød <robin@smidsrod.no>
 * 
 */
public class Range {

	private double min = 0;
	private boolean calculateMin = true;

	private double max = 0;
	private boolean calculateMax = true;

	private String unit = "";
	private String name = "";

	private double gridlineDistance = 0;
	private int gridlineCount = 0;

	private int dimension;
	private List<Item> itemList;

	/**
	 * @param dimension
	 *            The axis this Range represents. The first dimension should use
	 *            index 0.
	 * @param itemList
	 *            The list of items that is used to calculate minimum/maximum
	 *            constraints.
	 */
	public Range(int dimension, List<Item> itemList) {
		this.dimension = dimension;
		this.itemList = itemList;
	}

	/**
	 * @param min
	 *            Set the axis minimum value to the specified value.
	 * 
	 *            Side-effect: Disables calculation of minimum value.
	 */
	public void setMin(double min) {
		this.calculateMin = false;
		this.min = min;
	}

	/**
	 * @param max
	 *            Set the axis maximum value to the specified value.
	 * 
	 *            Side-effect: Disables calculation of maximum value.
	 */
	public void setMax(double max) {
		this.calculateMax = false;
		this.max = max;
	}

	/**
	 * @return Returns the specified minimum value or the calculated minimum
	 *         value based on the item list.
	 */
	public double getMin() {
		if (!calculateMin) {
			return min;
		}
		return calcMin();
	}

	/**
	 * @return Returns the specified maximum value or the calculated maximum
	 *         value based on the item list.
	 */
	public double getMax() {
		if (!calculateMax) {
			return max;
		}
		return calcMax();
	}

	/**
	 * @return Calculates the minimum value based on the item's values for the
	 *         given dimension.
	 */
	private double calcMin() {
		double savedMin = 0;
		for (Item i : itemList) {
			for (Value v : i.getValueList()) {
				double currentMin = v.getValue(dimension);
				if (currentMin < savedMin) {
					savedMin = currentMin;
				}
			}
		}
		return savedMin;
	}

	/**
	 * @return Calculates the maximum value based on the item's values for the
	 *         given dimension.
	 */
	private double calcMax() {
		double savedMax = 0;
		for (Item i : itemList) {
			for (Value v : i.getValueList()) {
				double currentMax = v.getValue(dimension);
				if (currentMax > savedMax) {
					savedMax = currentMax;
				}
			}
		}
		return savedMax;
	}

	public String getUnit() {
		return unit;
	}

	/**
	 * @param unit
	 *            Specifies the unit string for this specified range.
	 * 
	 *            Example: km, USD, dl, gallon, km/h
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            Specifies the name of this range.
	 * 
	 *            Example: age, amount, salary
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns true if either gridlineDistance or gridlineCount is
	 *         non-zero.
	 */
	public boolean hasGridlines() {
		return (gridlineDistance > 0 || gridlineCount > 0) ? true : false;
	}

	/**
	 * @param gridlineDistance
	 *            Set the distance between the grid lines to the specified
	 *            distance. Set to 0 to disable grid lines.
	 * 
	 *            Side-effect: Will calculate gridlineCount.
	 */
	public void setGridlineDistance(double gridlineDistance) {
		if (gridlineDistance > 0) {
			this.gridlineDistance = gridlineDistance;
			this.gridlineCount = (int) (calcRange() / gridlineDistance);
		} else {
			this.gridlineDistance = 0;
			this.gridlineCount = 0;
		}
	}

	/**
	 * @param gridlineCount
	 *            Sets the amount of grid lines to the specified amount. Set to
	 *            0 to disable grid lines.
	 * 
	 *            Side-effect: Will calculate the grid line distance.
	 */
	public void setGridlineCount(int gridlineCount) {
		if (gridlineCount > 0) {
			this.gridlineCount = gridlineCount;
			this.gridlineDistance = calcRange() / gridlineCount;
		} else {
			this.gridlineCount = 0;
			this.gridlineDistance = 0;
		}
	}

	public double getGridlineDistance() {
		return gridlineDistance;
	}

	public int getGridlineCount() {
		return gridlineCount;
	}

	/**
	 * @return The difference between the maximum and minimum value as an absolute
	 *         value.
	 */
	private double calcRange() {
		double max = getMax();
		double min = getMin();
		return Math.abs(max - min);
	}

}
