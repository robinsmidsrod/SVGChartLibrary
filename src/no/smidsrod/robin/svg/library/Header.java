package no.smidsrod.robin.svg.library;

import org.w3c.dom.Element;

class Header {

	static final int FONT_SIZE = 32;

	static int calcHeight() {
		return Canvas.MARGIN + FONT_SIZE;
	}

	static void createElement(Element svg, Chart chart) {
		// Create a text element for the title
		Element text = DOMBuilder.createElement(svg, "text");
		text.setAttribute("x", "50%");
		text.setAttribute("y", ( Canvas.MARGIN + FONT_SIZE ) + "");
		text.setAttribute("font-family", "sans-serif");
		text.setAttribute("font-size", FONT_SIZE + "");
		text.setAttribute("text-anchor", "middle");
		text.setTextContent(chart.getTitle());
		svg.appendChild(text);
	}

}
