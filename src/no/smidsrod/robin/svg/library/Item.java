package no.smidsrod.robin.svg.library;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Each item represents a colored element in the chart.
 * <p>
 * Charts contain usually several items. Each item represents e.g. a car brand,
 * a person, an employee or some other noun you're trying to show some
 * statistics for. Each item gets a separate color in the chart (either
 * specified or calculated).
 * 
 * @author Robin Smidsrød <robin@smidsrod.no>
 * 
 */
public class Item {

	public static final Color DEFAULT_COLOR = new Color(0, true); // Completely
	// transparent

	private String name = "";

	private Color color = DEFAULT_COLOR;

	private boolean highlighted = false;

	private List<Value> valueList = new ArrayList<Value>();

	/**
	 * @param name
	 *            Create an instance of Item with the given name.
	 */
	public Item(String name) {
		setName(name);
	}

	/**
	 * @return The name of this Item - default is the empty string
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            Set the name of the Item. If you specify a null value, it will
	 *            be converted to the empty string.
	 */
	public void setName(String name) {
		this.name = (name == null) ? "" : name;
	}

	/**
	 * @return Get the current color assigned to the Item.
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @param color
	 *            Set the Item to the specified color. If you specify a null
	 *            value, the Item will become completely transparent.
	 */
	public void setColor(Color color) {
		this.color = (color == null) ? DEFAULT_COLOR : color;
	}

	/**
	 * @return Returns true if this Item is supposed to be highlighted in the
	 *         chart.
	 */
	public boolean isHighlighted() {
		return highlighted;
	}

	/**
	 * @param highlighted
	 *            Specify true if you want this Item highlighted in the chart.
	 *            The default is false.
	 */
	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
	}

	/**
	 * @return Returns a list of Value objects that can be manipulated via the
	 *         List interface methods. Each Value instance can contain a value
	 *         for each dimension the chart class supports. This makes it quite
	 *         easy to store information for multi-dimensional charts.
	 */
	public List<Value> getValueList() {
		return valueList;
	}

}
