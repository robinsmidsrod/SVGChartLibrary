package no.smidsrod.robin.svg.library;

import java.awt.Point;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import org.w3c.dom.Element;

class Axis {

	private static final int MARKER_FONT_SIZE = 12;

	/**
	 * @param range
	 *            The range you want to calculate the label for.
	 * @return The calculated label.
	 */
	static String calcLabel(Range range) {

		// Nothing specified, return empty string
		if (range.getName().isEmpty() && range.getUnit().isEmpty()) {
			return "";
		}

		String rangeText = "";

		if (!range.getName().isEmpty()) {
			rangeText += range.getName();
		}

		if (!range.getUnit().isEmpty()) {
			rangeText += " (" + range.getUnit() + ")";
		}

		return rangeText;
	}

	static void createVerticalElement(Element svg, Chart chart) {
		Range range = chart.getRange(0);
		List<Item> items = chart.getItemList();

		// Vertical line
		int lineX = DataRegion.calcLeft();
		Point start = new Point(lineX, DataRegion.calcBottom());
		Point end = new Point(lineX, DataRegion.calcTop());
		SVGBuilder.createLineElement(svg, start, end, "black");

		// Grid markers + labels
		int width = DataRegion.calcWidth(items);
		boolean hasGridlines = range.hasGridlines();
		int gridLineCount = hasGridlines ? range.getGridlineCount() : 3;
		createVerticalMarkers(svg, range, start, gridLineCount, hasGridlines,
				width);

		// Draw minimum value grid marker
		createVerticalMarkerElement(svg, start, range.getMin());

		// Axis label
		int labelFontSize = 14;
		int yOffset = -labelFontSize - labelFontSize / 6;
		Point labelPosition = new Point(end.x, end.y + yOffset);
		String labelText = Axis.calcLabel(range);
		SVGBuilder.createTextElement(svg, labelPosition, labelText,
				labelFontSize, "start");

	}

	static private void createVerticalMarkerElement(Element svg,
			Point location, double value) {
		Point lineEnd = new Point(location.x + 3, location.y);
		Point textPosition = new Point(location.x + 7, location.y);
		SVGBuilder.createLineElement(svg, location, lineEnd, "black");
		SVGBuilder.createTextElement(svg, textPosition, formatValue(value),
				MARKER_FONT_SIZE, "start");
	}

	static private void createVerticalMarkers(Element svg, Range range,
			Point start, int gridLineCount, boolean hasGridlines, int width) {
		double scaleFactor = Axis.calcScaleFactor(range);
		double totalDistance = range.calcTotalDistance();
		double markerDistance = totalDistance / (gridLineCount + 1);
		double minValue = range.getMin();
		// Start at 1 and go to gridLineCount + 1 to draw max as well
		for (int i = 1; i <= gridLineCount + 1; i++) {
			double realMarkerDistance = markerDistance * i * scaleFactor;
			Point position = new Point(start.x, start.y
					- (int) realMarkerDistance);
			if (hasGridlines) {
				Point end = new Point(position.x + width, position.y);
				SVGBuilder.createDottedLineElement(svg, position, end, "black");
			}
			double value = minValue + markerDistance * i;
			createVerticalMarkerElement(svg, position, value);
		}
	}

	static private double calcScaleFactor(Range range) {
		return DataRegion.calcHeight() / range.calcTotalDistance();
	}

	static private String formatValue(double value) {
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
		nf.setMaximumFractionDigits(2);
		return nf.format(value);
	}

}
