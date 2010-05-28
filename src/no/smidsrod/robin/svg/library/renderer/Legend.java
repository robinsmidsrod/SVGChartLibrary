package no.smidsrod.robin.svg.library.renderer;

import java.util.List;

import no.smidsrod.robin.svg.library.Item;

import org.w3c.dom.Element;

class Legend {

	static final int FONT_SIZE = 24;

	/**
	 * Create a box with colors and names of all the items in the chart.
	 * 
	 * @param svg
	 *            the element to append the box to
	 * @param items
	 *            The list of items to calculate from
	 */
	static void createElement(Element svg, List<Item> items) {
		double x = 0;
		double y = 0;
		double width = calcWidth(items);
		double height = calcHeight(items);

		double boxX = Canvas.WIDTH - Canvas.MARGIN - width;
		double boxY = DataRegion.calcTop();

		// Wrap legend box in a container so we can offset it easier
		Element g = DOMBuilder.createElement(svg, "g");
		g.setAttribute("transform", "translate(" + boxX + " " + boxY + ")");
		svg.appendChild(g);

		Element box = DOMBuilder.createElement(g, "rect");
		box.setAttribute("x", x + "");
		box.setAttribute("y", y + "");
		box.setAttribute("width", width + "");
		box.setAttribute("height", height + "");
		box.setAttribute("stroke", "black");
		box.setAttribute("stroke-width", "1");
		box.setAttribute("fill", "none");
		g.appendChild(box);

		int counter = 0;
		for (Item item : items) {
			createItemElement(g, item, counter);
			counter++;
		}

	}

	static private void createItemElement(Element g, Item item, int counter) {
		// Can't fit any more items into the legend
		boolean tooManyItems = counter  > 23 ? true : false;
		if ( tooManyItems  ) {
			return;
		}
		double y = (counter + 1) * FONT_SIZE + (FONT_SIZE / 3);

		Element circle = DOMBuilder.createElement(g, "circle");
		circle.setAttribute("cx", "15");
		circle.setAttribute("cy", (y - 9) + "");
		circle.setAttribute("r", (FONT_SIZE * 0.4) + "");
		circle.setAttribute("fill", SVGUtil.cssColor(item.getColor()));
		circle.setAttribute("stroke", "black");
		g.appendChild(circle);

		Element text = DOMBuilder.createElement(g, "text");
		text.setAttribute("x", (FONT_SIZE * 1.25) + "");
		text.setAttribute("y", y + "");
		text.setAttribute("font-family", "sans-serif");
		text.setAttribute("font-size", FONT_SIZE + "");
		text.setAttribute("stroke", "black");
		text.setTextContent(item.getName());
		g.appendChild(text);
	}

	private static int calcMaxItemNameLength(List<Item> items) {
		int maxLength = 0;
		for (Item item : items) {
			int currentLength = item.getName().length();
			if (currentLength > maxLength) {
				maxLength = currentLength;
			}
		}
		return maxLength;
	}

	static double calcWidth(List<Item> items) {
		// Max 20% of canvas width
		double maxWidth = Canvas.WIDTH / 5;
		// Calculate max character count for Item label
		int maxLength = calcMaxItemNameLength(items);
		// Sans-serif fonts have approx. 50% width compared to height
		double circleWidth = 15 + FONT_SIZE * 0.4 * 2;
		double width = FONT_SIZE * 0.50 * maxLength + circleWidth + 10;

		return width > maxWidth ? maxWidth : width;
	}

	static private double calcHeight(List<Item> items) {
		// Maximum height is the same as the data region
		double maxHeight = DataRegion.calcHeight();

		// Use item count + 1 multiplied by font height
		double height = FONT_SIZE * ((double) items.size() + 1);

		return height > maxHeight ? maxHeight : height;
	}

}
