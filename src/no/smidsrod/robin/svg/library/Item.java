package no.smidsrod.robin.svg.library;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Robin Smidsrød <robin@smidsrod.no>
 *
 */
public class Item {

	private String name = "";

	private Color color = new Color(0, true); // Completely transparent

	private boolean highlighted = false;

	private List<Value> valueList = new ArrayList<Value>();

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
		this.color = (color == null) ? new Color(0, true) : color;
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
