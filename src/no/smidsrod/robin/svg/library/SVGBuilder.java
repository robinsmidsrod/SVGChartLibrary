package no.smidsrod.robin.svg.library;

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

}
