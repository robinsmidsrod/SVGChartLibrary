package no.smidsrod.robin.svg.library;

import java.awt.Color;

public class SVGUtil {

	/**
	 * 
	 * @param color
	 *            An AWT color instance
	 * @return CSS representation of the specified color
	 */
	public static String cssColor(Color color) {
		return "#" + Integer.toHexString(color.getRGB());
	}

}
