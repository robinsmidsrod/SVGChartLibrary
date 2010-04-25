package no.smidsrod.robin.svg.library;

import java.awt.Color;

class SVGUtil {

	/**
	 * Convert AWT color to CSS color.
	 * 
	 * @param color
	 *            An AWT color instance
	 * @return CSS representation of the specified color
	 */
	static String cssColor(Color color) {
		return "#" + Integer.toHexString(color.getRGB());
	}

}
