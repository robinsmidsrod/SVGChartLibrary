package no.smidsrod.robin.svg.library;

import java.awt.Point;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

class SVGBuilder {

	/**
	 * The viewport is set so that we have a virtual canvas to paint on sized
	 * according to Canvas.WIDTH/HEIGHT which preserves aspect ratio.
	 * 
	 * http://www.w3.org/TR/SVG/coords.html#ViewBoxAttribute
	 * http://www.w3.org/TR/SVG/coords.html#PreserveAspectRatioAttribute
	 * 
	 * @param doc
	 *            The XML document the SVG container should be appended to
	 * @return The SVG container itself
	 */
	static Element createRootElement(Document doc) {
		Element svg = doc.createElementNS(SVGRenderer.SVG_NAMESPACE, "svg");
		svg.setAttribute("version", SVGRenderer.SVG_VERSION);

		// Specify virtual canvas size
		String viewBox = "0 0 " + Canvas.WIDTH + " " + Canvas.HEIGHT;
		svg.setAttribute("viewBox", viewBox);

		// Center viewbox in the middle of viewport
		// svg.setAttribute("preserveAspectRatio", "xMidYMid");

		// Put viewbox in the top/left part of viewport
		svg.setAttribute("preserveAspectRatio", "xMinYMin");

		doc.appendChild(svg);

		return svg;

	}

	/**
	 * Set normal <title> and <desc> SVG metadata tags. Conformant UAs should
	 * render a title for the document with this information.
	 * 
	 * @param svg
	 *            The element to append the title and description to
	 * @param chart
	 *            The chart that contains the title and description
	 */
	static void createTitleAndDescription(Element svg, Chart chart) {
		Element title = DOMBuilder.createElement(svg, "title");
		Element desc = DOMBuilder.createElement(svg, "desc");
		title.setTextContent(chart.getTitle());
		desc.setTextContent(chart.getDescription());
		svg.appendChild(title);
		svg.appendChild(desc);
	}

	static void createTextElement(Element svg, Point position, String label,
			int fontSize, String anchor) {

		int yOffset = fontSize / 2 - (fontSize / 6);

		Element text = DOMBuilder.createElement(svg, "text");
		text.setAttribute("x", position.x + "");
		text.setAttribute("y", position.y + yOffset + "");
		text.setAttribute("stroke", "black");
		text.setAttribute("text-anchor", anchor);
		text.setAttribute("font-family", "sans-serif");
		text.setAttribute("font-size", fontSize + "");
		text.setTextContent(label);
		svg.appendChild(text);
	}

	static void createLineElement(Element svg, Point start, Point end,
			String color) {
		createLineElement(svg, start, end, color, "");
	}

	static void createLineElement(Element svg, Point start, Point end,
			String color, String filter) {
		Element line = DOMBuilder.createElement(svg, "line");
		line.setAttribute("x1", start.x + "");
		line.setAttribute("y1", start.y + "");
		line.setAttribute("x2", end.x + "");
		line.setAttribute("y2", end.y + "");
		line.setAttribute("stroke", color);
		if (!(filter == null || filter.isEmpty())) {
			line.setAttribute("filter", filter);
		}
		svg.appendChild(line);
	}

	static void createDottedLineElement(Element svg, Point start, Point end,
			String color) {
		Element line = DOMBuilder.createElement(svg, "line");
		line.setAttribute("x1", start.x + "");
		line.setAttribute("y1", start.y + "");
		line.setAttribute("x2", end.x + "");
		line.setAttribute("y2", end.y + "");
		line.setAttribute("stroke", color);
		line.setAttribute("stroke-dasharray", "2,2");
		svg.appendChild(line);
	}

	static void createCircleElement(Element svg, Point center, double radius,
			String color, String filter) {
		Element circle = DOMBuilder.createElement(svg, "circle");
		circle.setAttribute("cx", center.x + "");
		circle.setAttribute("cy", center.y + "");
		circle.setAttribute("r", radius + "");
		circle.setAttribute("fill", color);
		if (!(filter == null || filter.isEmpty())) {
			circle.setAttribute("filter", filter);
		}
		svg.appendChild(circle);

	}

	static void createDotElement(Element svg, Point center, String color,
			String filter) {
		createCircleElement(svg, center, 3, color, filter);
	}

	static void createHighlightFilterElement(Element svg) {
		Element defs = DOMBuilder.createElement(svg, "defs");
		svg.appendChild(defs);

		// Glow shadow taken from
		// http://commons.oreilly.com/wiki/index.php/SVG_Essentials/Filters#Creating_a_Glowing_Shadow
		// but modified further.

		Element filter = DOMBuilder.createElement(defs, "filter");
		filter.setAttribute("id", "highlight");
		defs.appendChild(filter);

		// Increase the radius of our graphic by 2 pixels
		Element morph = DOMBuilder.createElement(filter, "feMorphology");
		morph.setAttribute("operator", "dilate");
		morph.setAttribute("radius", "2");
		filter.appendChild(morph);

		// Make the original color stronger
		Element colorMatrix = DOMBuilder.createElement(filter, "feColorMatrix");
		colorMatrix.setAttribute("type", "saturate");
		colorMatrix.setAttribute("values", "1");
		filter.appendChild(colorMatrix);

		Element blur = DOMBuilder.createElement(filter, "feGaussianBlur");
		blur.setAttribute("stdDeviation", "2.5");
		blur.setAttribute("result", "coloredBlur");
		filter.appendChild(blur);

		Element merge = DOMBuilder.createElement(filter, "feMerge");
		filter.appendChild(merge);

		Element mergeNode1 = DOMBuilder.createElement(merge, "feMergeNode");
		mergeNode1.setAttribute("in", "coloredBlur");
		merge.appendChild(mergeNode1);

		Element mergeNode2 = DOMBuilder.createElement(merge, "feMergeNode");
		mergeNode2.setAttribute("in", "SourceGraphic");
		merge.appendChild(mergeNode2);

	}

}
