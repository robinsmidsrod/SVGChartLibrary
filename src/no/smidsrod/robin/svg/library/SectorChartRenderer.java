package no.smidsrod.robin.svg.library;

import java.awt.Point;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import org.w3c.dom.Element;

public class SectorChartRenderer extends AbstractSVGRenderer {

	// Traditional 4:3 aspect (landscape)
	private static final int CANVAS_WIDTH = 1000;
	private static final int CANVAS_HEIGHT = 750;
	private static final int CANVAS_MARGIN = 35;

	private static final int HEADER_FONT_SIZE = 32;
	private static final int FOOTER_FONT_SIZE = 18;
	private static final int LEGEND_FONT_SIZE = 24;

	private static final double HIGHLIGHTED_SECTOR_OFFSET_FACTOR = 0.25;

	private static final int SECTOR_MARGIN = 100;
	private static final int SECTOR_LABEL_FONT_SIZE = 24;
	private static final double SMALL_SECTOR_THRESHOLD = 0.04;

	private static final double LARGE_SECTOR_OFFSET_FACTOR = 0.8;
	private static final double HIGHLIGHTED_LARGE_SECTOR_OFFSET_FACTOR = 0.85;

	private static final double SMALL_SECTOR_OFFSET_FACTOR = 1.2;
	private static final double HIGHLIGHTED_SMALL_SECTOR_OFFSET_FACTOR = 1.15;

	private Chart chart;

	public SectorChartRenderer(Chart chart) {
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

	protected void buildSVGDocument() {
		ChartUtil.finalizeChart(chart); // Do necessary chart calculations
		createSVGElement(); // SVG root container
		createSVGHeader(); // Title and description tags
		createCanvasBorder();
		createTitleElement(); // Header text
		createItemLegend();
		createUnitLegend();
		createTotalLegend();
		createSectors(); // for all items
	}

	/**
	 * The viewport is set so that we have a virtual canvas to paint on sized
	 * according to VIEWBOX_WIDTH/HEIGHT which preserves aspect ratio.
	 * 
	 * http://www.w3.org/TR/SVG/coords.html#ViewBoxAttribute
	 * http://www.w3.org/TR/SVG/coords.html#PreserveAspectRatioAttribute
	 */
	private void createSVGElement() {
		Element svg = getXMLDocument().createElementNS(SVG_NAMESPACE, "svg");
		svg.setAttribute("version", SVG_VERSION);

		// Specify virtual canvas size
		String viewBox = "0 0 " + CANVAS_WIDTH + " " + CANVAS_HEIGHT;
		svg.setAttribute("viewBox", viewBox);

		// Center viewbox in the middle of viewport
		// svg.setAttribute("preserveAspectRatio", "xMidYMid");

		// Put viewbox in the top/left part of viewport
		svg.setAttribute("preserveAspectRatio", "xMinYMin");

		getXMLDocument().appendChild(svg);

	}

	private void createSVGHeader() {
		Element svg = getXMLDocument().getDocumentElement();

		// Set the SVG title
		Element title = getXMLDocument().createElement("title");
		title.setTextContent(chart.getTitle());
		svg.appendChild(title);

		// Make the description a tooltip on the background
		Element desc = getXMLDocument().createElement("desc");
		desc.setTextContent(chart.getDescription());
		svg.appendChild(desc);
	}

	private void createCanvasBorder() {
		Element svg = getXMLDocument().getDocumentElement();

		Element border = getXMLDocument().createElement("rect");
		border.setAttribute("x", "1");
		border.setAttribute("y", "1");
		int width = CANVAS_WIDTH - 1;
		border.setAttribute("width", width + "");
		int height = CANVAS_HEIGHT - 1;
		border.setAttribute("height", height + "");
		border.setAttribute("stroke", "black");
		border.setAttribute("stroke-width", "1");
		border.setAttribute("fill", "none");
		svg.appendChild(border);
	}

	private void createTitleElement() {
		Element svg = getXMLDocument().getDocumentElement();

		// Create a text element for the title
		Element text = getXMLDocument().createElement("text");
		text.setAttribute("x", "50%");
		text.setAttribute("y", CANVAS_MARGIN + "");
		text.setAttribute("font-family", "sans-serif");
		text.setAttribute("font-size", HEADER_FONT_SIZE + "");
		text.setAttribute("text-anchor", "middle");
		text.setTextContent(chart.getTitle());
		svg.appendChild(text);

	}

	private void createItemLegend() {
		Element svg = getXMLDocument().getDocumentElement();

		double x = 0;
		double y = 0;
		double width = calcLegendWidth();
		double height = calcLegendHeight();

		double offsetRight = CANVAS_WIDTH - width - CANVAS_MARGIN;

		// Wrap legend box in a container so we can offset it easier
		Element g = getXMLDocument().createElement("g");
		g.setAttribute("transform", "translate(" + offsetRight + " 75)");
		svg.appendChild(g);

		Element box = getXMLDocument().createElement("rect");
		box.setAttribute("x", x + "");
		box.setAttribute("y", y + "");
		box.setAttribute("width", width + "");
		box.setAttribute("height", height + "");
		box.setAttribute("stroke", "black");
		box.setAttribute("stroke-width", "1");
		box.setAttribute("fill", "none");
		g.appendChild(box);

		List<Item> items = chart.getItemList();
		int counter = 0;
		for (Item item : items) {
			drawLegendItem(counter, item, g);
			counter++;
		}

	}

	private void createUnitLegend() {

		String unitText = chart.getRange(0).getUnit();
		if (unitText.isEmpty()) {
			return;
		}

		Element svg = getXMLDocument().getDocumentElement();

		// bottom/right
		int labelX = CANVAS_WIDTH - CANVAS_MARGIN;
		int labelY = CANVAS_HEIGHT - CANVAS_MARGIN;

		Element unitLegend = getXMLDocument().createElement("text");
		unitLegend.setAttribute("x", labelX + "");
		unitLegend.setAttribute("y", labelY + "");
		unitLegend.setAttribute("text-anchor", "end");
		unitLegend.setAttribute("font-family", "sans-serif");
		unitLegend.setAttribute("font-size", FOOTER_FONT_SIZE + "");
		unitLegend.setAttribute("stroke", "black");
		unitLegend.setTextContent("Unit: " + unitText);

		svg.appendChild(unitLegend);

	}

	private void createTotalLegend() {
		Element svg = getXMLDocument().getDocumentElement();

		// bottom/left (two lines of text)
		int labelX = CANVAS_MARGIN;
		int labelY = CANVAS_HEIGHT - CANVAS_MARGIN - FOOTER_FONT_SIZE;

		NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
		nf.setMaximumFractionDigits(2);

		double sumValues = calcSumValues(chart.getItemList());

		Element totalSum = getXMLDocument().createElement("text");
		totalSum.setAttribute("x", labelX + "");
		totalSum.setAttribute("y", labelY + "");
		totalSum.setAttribute("font-family", "sans-serif");
		totalSum.setAttribute("font-size", FOOTER_FONT_SIZE + "");
		totalSum.setAttribute("stroke", "black");
		totalSum.setTextContent("Total sum: " + nf.format(sumValues));

		svg.appendChild(totalSum);

		Element totalCount = getXMLDocument().createElement("text");
		totalCount.setAttribute("x", labelX + "");
		totalCount.setAttribute("y", labelY + FOOTER_FONT_SIZE + "");
		totalCount.setAttribute("font-family", "sans-serif");
		totalCount.setAttribute("font-size", FOOTER_FONT_SIZE + "");
		totalCount.setAttribute("stroke", "black");
		totalCount.setTextContent("Total items: " + chart.getItemList().size());

		svg.appendChild(totalCount);

	}

	private void createSectors() {
		Element svg = getXMLDocument().getDocumentElement();

		Element g = getXMLDocument().createElement("g");
		svg.appendChild(g);

		Point center = calcChartCenter();
		double radius = calcSectorRadius();

		List<Item> items = chart.getItemList();
		double sumValues = calcSumValues(items);
		double startValue = 0;
		for (Item item : items) {
			double currentValue = getValueForItem(item);
			double startPosition = calcPosition(startValue, sumValues);
			double endPosition = calcPosition(startValue + currentValue,
					sumValues);
			drawSectorItem(center, radius, startPosition, endPosition, item, g);
			startValue += currentValue;
		}
	}

	private void drawLegendItem(int counter, Item item, Element g) {
		double y = (counter + 1) * LEGEND_FONT_SIZE + (LEGEND_FONT_SIZE / 3);

		Element circle = getXMLDocument().createElement("circle");
		circle.setAttribute("cx", "15");
		circle.setAttribute("cy", (y - 9) + "");
		circle.setAttribute("r", (LEGEND_FONT_SIZE * 0.4) + "");
		circle.setAttribute("fill", SVGUtil.cssColor(item.getColor()));
		g.appendChild(circle);

		Element text = getXMLDocument().createElement("text");
		text.setAttribute("x", (LEGEND_FONT_SIZE * 1.25) + "");
		text.setAttribute("y", y + "");
		text.setAttribute("font-family", "sans-serif");
		text.setAttribute("font-size", LEGEND_FONT_SIZE + "");
		text.setAttribute("stroke", "black");
		text.setTextContent(item.getName());
		g.appendChild(text);
	}

	private void drawSectorItem(Point center, double radius, double start,
			double end, Item item, Element g) {

		String percentage = formatPercentage(start, end) + "%";

		String pathData = calcPathData(center, start, end, radius, item
				.isHighlighted());

		Element path = getXMLDocument().createElement("path");
		path.setAttribute("d", pathData);
		path.setAttribute("fill", SVGUtil.cssColor(item.getColor()));
		path.setAttribute("stroke", "black");
		g.appendChild(path);

		double middle = calcMiddle(start, end);
		double labelRadius = calcLabelRadius(radius, isSmallSector(start, end),
				item.isHighlighted());
		int labelX = calcXFromFraction(middle, center, labelRadius);
		int labelY = calcYFromFraction(middle, center, labelRadius);

		// Slight offset for Y based on font size
		int labelYOffset = SECTOR_LABEL_FONT_SIZE / 3;

		Element label = getXMLDocument().createElement("text");
		label.setAttribute("x", labelX + "");
		label.setAttribute("y", labelY + labelYOffset + "");
		label.setAttribute("text-anchor", "middle");
		label.setAttribute("font-family", "sans-serif");
		label.setAttribute("font-size", SECTOR_LABEL_FONT_SIZE + "");
		label.setAttribute("stroke", "black");
		label.setTextContent(percentage);

		g.appendChild(label);

	}

	private String calcPathData(Point center, double start, double end,
			double radius, boolean isHighlighted) {

		int offsetX = 0;
		int offsetY = 0;

		if (isHighlighted) {
			double middle = calcMiddle(start, end);
			offsetX = calcXFromFraction(middle, center, radius
					* HIGHLIGHTED_SECTOR_OFFSET_FACTOR)
					- center.x;
			offsetY = calcYFromFraction(middle, center, radius
					* HIGHLIGHTED_SECTOR_OFFSET_FACTOR)
					- center.y;
		}

		int centerX = center.x + offsetX;
		int centerY = center.y + offsetY;

		int startX = calcXFromFraction(start, center, radius) + offsetX;
		int startY = calcYFromFraction(start, center, radius) + offsetY;

		int endX = calcXFromFraction(end, center, radius) + offsetX;
		int endY = calcYFromFraction(end, center, radius) + offsetY;

		// If the sector is larger than 50%, make sure we actually get the right
		// component of the arc, see
		// http://www.w3.org/TR/SVG/paths.html#PathDataEllipticalArcCommands
		String arcPart = Math.abs(start - end) > 0.5 ? " 1,1" : " 0,1";

		String moveToCenter = "M" + centerX + "," + centerY;
		String lineToStart = " L" + startX + "," + startY;
		String arcToEnd = " A" + radius + "," + radius + " 0" + arcPart + endX
				+ "," + endY;
		String closePath = " Z";

		return moveToCenter + lineToStart + arcToEnd + closePath;
	}

	private double calcLegendWidth() {
		// Max 20% of canvas width
		double maxWidth = CANVAS_WIDTH / 5;

		// Calculate max character count for Item label
		List<Item> items = chart.getItemList();
		int maxLength = 0;
		for (Item item : items) {
			int currentLength = item.getName().length();
			if (currentLength > maxLength) {
				maxLength = currentLength;
			}
		}

		// Sans-serif fonts have approx. 50% width compared to height
		// (estimated)
		double circleWidth = 15 + LEGEND_FONT_SIZE * 0.4 * 2;
		double width = LEGEND_FONT_SIZE * 0.50 * maxLength + circleWidth + 10;

		return width > maxWidth ? maxWidth : width;
	}

	private double calcLegendHeight() {
		// Max 75% of height
		double maxHeight = CANVAS_HEIGHT * 3 / 4;

		// Use item count + 1 multiplied by font height
		int itemCount = chart.getItemList().size();
		double height = LEGEND_FONT_SIZE * ((double) itemCount + 1);

		return height > maxHeight ? maxHeight : height;
	}

	private int calcHeaderHeight() {
		return CANVAS_MARGIN + HEADER_FONT_SIZE;
	}

	private int calcFooterHeight() {
		return CANVAS_MARGIN + FOOTER_FONT_SIZE * 2;
	}

	/**
	 * Calculates the center point for the sector elements, offset by header,
	 * footer and item legend.
	 * 
	 * @return center-point for the sectors
	 */
	private Point calcChartCenter() {
		int x = CANVAS_WIDTH / 2 - (int) (calcLegendWidth() / 2);
		int y = ((CANVAS_HEIGHT - calcHeaderHeight() - calcFooterHeight()) / 2)
				+ calcHeaderHeight();
		return new Point(x, y);
	}

	private double calcSectorRadius() {
		return (CANVAS_HEIGHT - calcHeaderHeight() - calcFooterHeight() - SECTOR_MARGIN * 2) / 2;
	}

	private boolean isSmallSector(double start, double end) {
		return Math.abs(start - end) < SMALL_SECTOR_THRESHOLD ? true : false;
	}

	private double calcLabelRadius(double radius, boolean isSmallSector,
			boolean isHighlighted) {
		double labelRadius = radius;
		if (isHighlighted) {
			labelRadius = labelRadius * (1 + HIGHLIGHTED_SECTOR_OFFSET_FACTOR);
		}

		if (isSmallSector) {
			if (isHighlighted) {
				labelRadius = labelRadius
						* HIGHLIGHTED_SMALL_SECTOR_OFFSET_FACTOR;
			} else {
				labelRadius = labelRadius * SMALL_SECTOR_OFFSET_FACTOR;
			}
		} else {
			if (isHighlighted) {
				labelRadius = labelRadius
						* HIGHLIGHTED_LARGE_SECTOR_OFFSET_FACTOR;
			} else {
				labelRadius = labelRadius * LARGE_SECTOR_OFFSET_FACTOR;
			}
		}

		return labelRadius;
	}

	private double calcMiddle(double start, double end) {
		return (end - start) / 2 + start;
	}

	private int calcXFromFraction(double fraction, Point center, double radius) {
		int angle = calcAngle(fraction);
		// From http://math2.org/math/geometry/circles.htm
		// x(t) = r cos(t) + j
		return (int) (radius * Math.cos(Math.toRadians(angle)) + center.x);
	}

	private int calcYFromFraction(double fraction, Point center, double radius) {
		int angle = calcAngle(fraction);
		// From http://math2.org/math/geometry/circles.htm
		// y(t) = r sin(t) + k
		return (int) (radius * Math.sin(Math.toRadians(angle)) + center.y);
	}

	private double calcPosition(double value, double total) {
		return value / total;
	}

	private double getValueForItem(Item item) {
		if (item.getValueList().size() > 0) {
			// Always use the first dimension of the first Value instance
			return item.getValueList().get(0).get(chart.getRange(0));
		}
		throw new RuntimeException("There is no value for this item");
	}

	private double calcSumValues(List<Item> items) {
		double sumValues = 0;
		for (Item item : items) {
			sumValues += getValueForItem(item);
		}
		return sumValues;
	}

	private int calcAngle(double fraction) {
		return (int) Math.round(fraction * 360);
	}

	private String formatPercentage(double start, double end) {
		double fraction = Math.abs(start - end);
		double percentage = fraction * 100;

		NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
		if (fraction < SMALL_SECTOR_THRESHOLD) {
			nf.setMaximumFractionDigits(1);
		} else {
			nf.setMaximumFractionDigits(0);
		}

		return nf.format(percentage);
	}

}
