package no.smidsrod.robin.svg.library.renderer;

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
		String red = Integer.toHexString(color.getRed());
		String green = Integer.toHexString(color.getGreen());
		String blue = Integer.toHexString(color.getBlue());
		String convertedColor = "#" + prefixZero(red) + prefixZero(green) + prefixZero(blue);
		return convertedColor;
	}

	private static String prefixZero(String str) {
		if ( str.length() > 2 ) {
			throw new RuntimeException("This string, '" + str + "', is not an appropriate hex number.");
		}
		if ( str.length() == 2 ) {
			return str;
		}
		if ( str.length() == 1 ) {
			return "0" + str;
		}
		return "00";
	}

}
