package no.smidsrod.robin.svg.library.renderer;

import java.awt.Point;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import no.smidsrod.robin.svg.library.Item;
import no.smidsrod.robin.svg.library.Range;

import org.w3c.dom.Element;

class Axis {

	private static final int MARKER_FONT_SIZE = 12;
	private static final int LABEL_FONT_SIZE = 14;

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

	static void createHorizontalElement(Element svg, Range range,
			List<Item> items) {

		// Horizontal line
		int lineY = DataRegion.calcBottom();
		Point start = new Point(DataRegion.calcLeft(), lineY);
		Point end = new Point(DataRegion.calcRight(items), lineY);
		SVGBuilder.createLineElement(svg, start, end, "black");

		// Grid markers + labels
		int height = DataRegion.calcHeight();
		boolean hasGridlines = range.hasGridlines();
		int gridLineCount = hasGridlines ? range.getGridlineCount() : 3;
		createHorizontalMarkers(svg, range, items, start, gridLineCount,
				hasGridlines, height);

		// Draw minimum value grid marker
		createHorizontalMarkerElement(svg, start, range.getMin());

		// Axis label
		int yOffset = +LABEL_FONT_SIZE - LABEL_FONT_SIZE / 6;
		Point labelPosition = new Point(end.x, end.y + yOffset);
		String labelText = calcLabel(range);
		SVGBuilder.createTextElement(svg, labelPosition, labelText,
				LABEL_FONT_SIZE, "end");

	}

	static void createVerticalElement(Element svg, Range range, List<Item> items) {

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
		int yOffset = -LABEL_FONT_SIZE - LABEL_FONT_SIZE / 6;
		Point labelPosition = new Point(end.x, end.y + yOffset);
		String labelText = calcLabel(range);
		SVGBuilder.createTextElement(svg, labelPosition, labelText,
				LABEL_FONT_SIZE, "start");

	}

	static private void createHorizontalMarkers(Element svg, Range range,
			List<Item> items, Point start, int gridLineCount,
			boolean hasGridlines, int height) {
		double scaleFactor = DataRegion.calcWidth(items)
				/ range.calcTotalDistance();
		double markerDistance = range.calcTotalDistance() / (gridLineCount + 1);
		// Start at 1 and go to gridLineCount + 1 to draw max as well
		for (int i = 1; i <= gridLineCount + 1; i++) {
			double realMarkerDistance = markerDistance * i * scaleFactor;
			Point position = new Point(start.x + (int) realMarkerDistance,
					start.y);
			if (hasGridlines) {
				Point end = new Point(position.x, position.y - height);
				SVGBuilder.createDottedLineElement(svg, position, end, "black");
			}
			double value = range.getMin() + markerDistance * i;
			createHorizontalMarkerElement(svg, position, value);
		}
	}

	static private void createVerticalMarkers(Element svg, Range range,
			Point start, int gridLineCount, boolean hasGridlines, int width) {
		double scaleFactor = DataRegion.calcHeight()
				/ range.calcTotalDistance();
		double markerDistance = range.calcTotalDistance() / (gridLineCount + 1);
		// Start at 1 and go to gridLineCount + 1 to draw max as well
		for (int i = 1; i <= gridLineCount + 1; i++) {
			double realMarkerDistance = markerDistance * i * scaleFactor;
			Point position = new Point(start.x, start.y
					- (int) realMarkerDistance);
			if (hasGridlines) {
				Point end = new Point(position.x + width, position.y);
				SVGBuilder.createDottedLineElement(svg, position, end, "black");
			}
			double value = range.getMin() + markerDistance * i;
			createVerticalMarkerElement(svg, position, value);
		}
	}

	static private void createHorizontalMarkerElement(Element svg,
			Point location, double value) {
		Point lineEnd = new Point(location.x, location.y - 3);
		Point textPosition = new Point(location.x, location.y - 7);
		SVGBuilder.createLineElement(svg, location, lineEnd, "black");
		SVGBuilder.createTextElement(svg, textPosition, formatValue(value),
				MARKER_FONT_SIZE, "middle");
	}

	static private void createVerticalMarkerElement(Element svg,
			Point location, double value) {
		Point lineEnd = new Point(location.x + 3, location.y);
		Point textPosition = new Point(location.x + 7, location.y);
		SVGBuilder.createLineElement(svg, location, lineEnd, "black");
		SVGBuilder.createTextElement(svg, textPosition, formatValue(value),
				MARKER_FONT_SIZE, "start");
	}

	static private String formatValue(double value) {
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
		nf.setMaximumFractionDigits(2);
		return nf.format(value);
	}

}
