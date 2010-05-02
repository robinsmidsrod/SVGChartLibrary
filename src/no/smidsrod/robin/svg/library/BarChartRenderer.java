package no.smidsrod.robin.svg.library;

import java.awt.Point;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import org.w3c.dom.Element;

public class BarChartRenderer extends AbstractSVGRenderer implements
		SVGRenderer {

	private Chart chart;

	public BarChartRenderer(Chart chart) {
		if (chart == null) {
			throw new NullPointerException(
					"Please specify an instance of the Chart interface");
		}
		if (chart.getDimensionCount() < 1) {
			throw new IndexOutOfBoundsException(
					"The specified chart instance doesn't support at least 1 dimension");
		}
		this.chart = chart;
	}

	@Override
	void buildSVGDocument() {
		ChartUtil.finalizeChart(chart);
		Element svg = SVGBuilder.createRootElement(getXMLDocument());
		SVGBuilder.createTitleAndDescription(svg, chart);
		Canvas.createBorderElement(svg);
		Header.createElement(svg, chart);
		Legend.createElement(svg, chart.getItemList());
		// DataRegion.createBorderElement(svg, chart.getItemList());

		createVerticalAxis(svg, chart);
		createHorizontalLine(svg, chart.getItemList());
		// createBars();
	}

	private void createVerticalAxis(Element svg, Chart chart) {
		Range range = chart.getRange(0);
		List<Item> items = chart.getItemList();

		// Vertical line
		int lineX = DataRegion.calcLeft();
		Point start = new Point(lineX, DataRegion.calcBottom());
		Point end = new Point(lineX, DataRegion.calcTop());
		createLineElement(svg, start, end, "black");

		// Grid markers + labels
		int width = DataRegion.calcWidth(items);
		boolean hasGridlines = range.hasGridlines();
		int gridLineCount = hasGridlines ? range.getGridlineCount() : 3;
		createAxisMarkers(svg, range, start, gridLineCount, hasGridlines, width);

		// Axis label
		int labelFontSize = 14;
		int yOffset = - labelFontSize - labelFontSize / 6;
		Point labelPosition = new Point(end.x, end.y + yOffset );
		String labelText = Axis.calcLabel(range);
		createTextElement(svg, labelPosition, labelText, labelFontSize, "start");

	}

	private void createAxisMarkers(Element svg, Range range, Point start,
			int gridLineCount, boolean drawGrid, int width) {
		int fontSize = 12;
		double totalDistance = Math.abs(range.getMax() - range.getMin());
		double scaleFactor = (double) DataRegion.calcHeight() / totalDistance;
		// Start at 0 and go to gridLineCount + 1 to draw min and max aswell
		for (int i = 0; i <= gridLineCount + 1; i++) {
			double distance = totalDistance / (gridLineCount + 1) * i
					* scaleFactor;
			double value = range.getMin()
					+ (totalDistance / (gridLineCount + 1) * i);
			Point loc = new Point(start.x, start.y - (int) distance);
			if (drawGrid) {
				createDottedLineElement(svg, loc, new Point(loc.x + width,
						loc.y), "black");
			}
			createLineElement(svg, loc, new Point(loc.x + 3, loc.y), "black");
			createTextElement(svg, new Point(loc.x + 7, loc.y),
					formatValue(value), fontSize, "start");
		}
	}

	private void createDottedLineElement(Element svg, Point start, Point end,
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

	private String formatValue(double value) {
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
		nf.setMaximumFractionDigits(2);
		return nf.format(value);
	}

	private void createLineElement(Element svg, Point start, Point end,
			String color) {
		Element line = DOMBuilder.createElement(svg, "line");
		line.setAttribute("x1", start.x + "");
		line.setAttribute("y1", start.y + "");
		line.setAttribute("x2", end.x + "");
		line.setAttribute("y2", end.y + "");
		line.setAttribute("stroke", color);
		svg.appendChild(line);
	}

	private void createTextElement(Element svg, Point position, String label,
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

	private void createHorizontalLine(Element svg, List<Item> items) {
		int startX = DataRegion.calcLeft();
		int endX = DataRegion.calcRight(items);
		int lineY = DataRegion.calcBottom();
		Point start = new Point(startX, lineY);
		Point end = new Point(endX, lineY);
		createLineElement(svg, start, end, "black");
	}

}
