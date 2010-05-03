package no.smidsrod.robin.svg.library;

import java.awt.Point;
import java.util.List;

import org.w3c.dom.Element;

class DataRegion {

	private static final int MARGIN = 20;

	/**
	 * @return The leftmost coordinate on the canvas of the data region.
	 */
	static int calcLeft() {
		return Canvas.MARGIN;
	}

	/**
	 * @param items
	 *            Required to calculate the width of the legend.
	 * @return The rightmost coordinate on the canvas of the data region.
	 */
	static int calcRight(List<Item> items) {
		return (int) (Canvas.WIDTH - Canvas.MARGIN - Legend.calcWidth(items) - MARGIN);
	}

	/**
	 * @return The topmost coordinate on the canvas of the data region.
	 */
	static int calcTop() {
		return Header.calcHeight() + MARGIN;
	}

	/**
	 * @return The bottommost coordinate on the canvas of the data region.
	 */
	static int calcBottom() {
		return Canvas.HEIGHT - Footer.calcHeight() - MARGIN;
	}

	/**
	 * @param items
	 *            Required to calculate the width of the legend.
	 * @return The width of the data region.
	 */
	static int calcWidth(List<Item> items) {
		return Math.abs(calcRight(items) - calcLeft());
	}

	/**
	 * @return The height of the data region.
	 */
	static int calcHeight() {
		return Math.abs(calcBottom() - calcTop());
	}

	/**
	 * Calculates the center point for the data region.
	 * 
	 * @param items
	 *            Required to calculate the width of the data region.
	 * 
	 * @return center-point for the data region.
	 */
	static Point calcCenter(List<Item> items) {
		int x = calcWidth(items) / 2 + calcLeft();
		int y = calcHeight() / 2 + calcTop();
		return new Point(x, y);
	}

	/**
	 * @param svg
	 *            The DOM element to append the border to
	 * @param items
	 *            Required to calculate the width of the box.
	 */
	static void createBorderElement(Element svg, List<Item> items) {
		Element box = DOMBuilder.createElement(svg, "rect");
		box.setAttribute("x", calcLeft() + "");
		box.setAttribute("y", calcTop() + "");
		box.setAttribute("width", calcWidth(items) + "");
		box.setAttribute("height", calcHeight() + "");
		box.setAttribute("stroke", "black");
		box.setAttribute("stroke-width", "1");
		box.setAttribute("fill", "none");
		svg.appendChild(box);
	}

	/**
	 * @param value
	 *            The value instance to fetch data from
	 * @param xRange
	 *            The range object that identifies the X axis
	 * @param yRange
	 *            The range object that identifies the Y axis
	 * @param xScaleFactor
	 * @param yScaleFactor
	 * @return
	 */
	static Point calcPoint(Value value, Range xRange, Range yRange,
			double xScaleFactor, double yScaleFactor) {
		double xOffset = (value.get(xRange) - xRange.getMin()) * xScaleFactor;
		double yOffset = (value.get(yRange) - yRange.getMin()) * yScaleFactor;
		int x = (int) (calcLeft() + xOffset);
		int y = (int) (calcBottom() - yOffset);
		return new Point(x, y);
	}

	public static void createClipElement(Element svg, List<Item> items) {
		Element defs = DOMBuilder.createElement(svg, "defs");
		svg.appendChild(defs);

		Element clip = DOMBuilder.createElement(defs, "clipPath");
		clip.setAttribute("id", "dataRegion");
		defs.appendChild(clip);

		int x = calcLeft();
		int y = calcTop();
		int width = calcWidth(items);
		int height = calcHeight();
		Element rect = DOMBuilder.createElement(clip, "rect");
		rect.setAttribute("x", x + "");
		rect.setAttribute("y", y + "");
		rect.setAttribute("width", width + "");
		rect.setAttribute("height", height + "");
		clip.appendChild(rect);

	}

}
