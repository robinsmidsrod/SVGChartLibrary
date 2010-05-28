package no.smidsrod.robin.svg.library.renderer;

import org.w3c.dom.Element;

class Canvas {

	// Traditional 4:3 aspect (landscape)
	static final int WIDTH = 1000;
	static final int HEIGHT = 750;

	// Margin between the canvas border and contained elements
	static final int MARGIN = 20;

	/**
	 * Paint a border around the outer edge of the canvas.
	 * 
	 * @param svg
	 *            The element that the canvas border will be applied to
	 */
	static void createBorderElement(Element svg) {
		int x = 1;
		int y = 1;
		int width = Canvas.WIDTH - x * 2;
		int height = Canvas.HEIGHT - y * 2;
	
		Element border = DOMBuilder.createElement(svg, "rect");
		border.setAttribute("x", x + "");
		border.setAttribute("y", y + "");
		border.setAttribute("width", width + "");
		border.setAttribute("height", height + "");
		border.setAttribute("stroke", "black");
		border.setAttribute("stroke-width", "1");
		border.setAttribute("fill", "none");
		svg.appendChild(border);
	}

}
