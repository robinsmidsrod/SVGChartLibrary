package no.smidsrod.robin.svg.library;

import java.awt.Color;
import java.awt.Point;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SectorChartRenderer extends AbstractSVGRenderer {

	// Traditional 4:3 aspect (landscape)
	private static final int VIEWBOX_WIDTH = 1000;
	private static final int VIEWBOX_HEIGHT = 750;

	private static final int HEADER_POSITION = 50;
	private static final int HEADER_FONT_SIZE = 32;
	private static final int HEADER_GUTTER = 24;

	private static final int LEGEND_FONT_SIZE = 24;

	private static final int SECTOR_MARGIN = 75;

	private static final String SVG_NAMESPACE = "http://www.w3.org/2000/svg";
	private static final String SVG_VERSION = "1.1";

	private Chart chart;
	private Document xmlDocument;
	private boolean prettyPrint = false;

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

	@Override
	public void renderSVG(OutputStream outputStream) {
		if (xmlDocument == null) {
			generateXML();
		}
		XMLSerializer serializer = new XMLSerializer(outputStream);
		serializer.setPrettyPrint(prettyPrint);
		serializer.write(xmlDocument);
	}

	public void setPrettyPrint(boolean prettyPrint) {
		this.prettyPrint = prettyPrint;
	}

	private void generateXML() {
		// Create new DOM object
		xmlDocument = DOMBuilder.newDocument();

		// Create SVG root element
		createSVGElement();

		// Title and description tags (typical SVG metadata)
		createHeader();

		// Draw viewbox border
		createViewBoxBorder();

		// Create title and description element
		createTitleElement();

		// calculate colors for each item
		calcItemColors();

		// Create legend
		createLegend();

		// create one sector for each item
		createSectors();
	}

	/**
	 * The viewport is set so that we have a virtual canvas to paint on sized
	 * according to VIEWBOX_WIDTH/HEIGHT which preserves aspect ratio.
	 * 
	 * http://www.w3.org/TR/SVG/coords.html#ViewBoxAttribute
	 * http://www.w3.org/TR/SVG/coords.html#PreserveAspectRatioAttribute
	 */
	private void createSVGElement() {
		Element svg = (Element) xmlDocument.createElementNS(SVG_NAMESPACE,
				"svg");
		xmlDocument.appendChild(svg);
		svg.setAttribute("version", SVG_VERSION);

		// Specify virtual canvas size
		String viewBox = "0 0 " + VIEWBOX_WIDTH + " " + VIEWBOX_HEIGHT;
		svg.setAttribute("viewBox", viewBox);

		// Center viewbox in the middle of viewport
		// svg.setAttribute("preserveAspectRatio", "xMidYMid");

		// Put viewbox in the top/left part of viewport
		svg.setAttribute("preserveAspectRatio", "xMinYMin");
	}

	private void createHeader() {
		Element svg = xmlDocument.getDocumentElement();

		// Set the SVG title
		Element title = xmlDocument.createElement("title");
		title.setTextContent(chart.getTitle());
		svg.appendChild(title);

		// Make the description a tooltip on the background
		Element desc = xmlDocument.createElement("desc");
		desc.setTextContent(chart.getDescription());
		svg.appendChild(desc);
	}

	private void createViewBoxBorder() {
		Element svg = xmlDocument.getDocumentElement();

		Element border = xmlDocument.createElement("rect");
		border.setAttribute("x", "1");
		border.setAttribute("y", "1");
		int width = VIEWBOX_WIDTH - 1;
		border.setAttribute("width", width + "");
		int height = VIEWBOX_HEIGHT - 1;
		border.setAttribute("height", height + "");
		border.setAttribute("stroke", "black");
		border.setAttribute("stroke-width", "1");
		border.setAttribute("fill", "none");
		svg.appendChild(border);
	}

	private void createTitleElement() {
		Element svg = xmlDocument.getDocumentElement();

		// Create a text element for the title
		Element text = xmlDocument.createElement("text");
		text.setAttribute("x", "50%");
		text.setAttribute("y", HEADER_POSITION + "");
		text.setAttribute("font-family", "sans-serif");
		text.setAttribute("font-size", HEADER_FONT_SIZE + "");
		text.setAttribute("text-anchor", "middle");
		text.setTextContent(chart.getTitle());
		svg.appendChild(text);

	}

	private void calcItemColors() {
		List<Item> items = chart.getItemList();
		int numberOfItems = 0;
		Color defaultColor = Item.DEFAULT_COLOR;

		// Figure out how many items that need colors calculated
		for (Item item : items) {
			if (item.getColor() == defaultColor) {
				numberOfItems++;
			}
		}

		// Set color on the items that needs it
		int i = 0;
		for (Item item : items) {
			if (item.getColor() == defaultColor) {
				item.setColor(calcColorForItem(i, numberOfItems));
				i++;
			}
		}

	}

	private Color calcColorForItem(int i, int numberOfItems) {
		if (numberOfItems == 0) {
			return Color.GRAY;
		}
		float interval = 1f / numberOfItems;
		float hue = interval * (float) i;
		// System.err.println("Interval: " + interval);
		// System.err.println("Hue: " + hue);
		return new Color(Color.HSBtoRGB(hue, 1f, 1f));
	}

	private void createLegend() {
		Element svg = xmlDocument.getDocumentElement();

		double x = 0;
		double y = 0;
		double width = calcLegendWidth();
		double height = calcLegendHeight();

		double offsetRight = VIEWBOX_WIDTH - width - 25;

		// Wrap legend box in a container so we can offset it easier
		Element g = xmlDocument.createElement("g");
		g.setAttribute("transform", "translate(" + offsetRight + " 75)");
		svg.appendChild(g);

		Element box = xmlDocument.createElement("rect");
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

	private void drawLegendItem(int counter, Item item, Element g) {
		double y = (counter + 1) * LEGEND_FONT_SIZE + (LEGEND_FONT_SIZE / 3);

		Element circle = xmlDocument.createElement("circle");
		circle.setAttribute("cx", "15");
		circle.setAttribute("cy", (y - 9) + "");
		circle.setAttribute("r", (LEGEND_FONT_SIZE * 0.4) + "");
		circle.setAttribute("fill", cssColor(item.getColor()));
		g.appendChild(circle);

		Element text = xmlDocument.createElement("text");
		text.setAttribute("x", (LEGEND_FONT_SIZE * 1.25) + "");
		text.setAttribute("y", y + "");
		text.setAttribute("font-family", "sans-serif");
		text.setAttribute("font-size", LEGEND_FONT_SIZE + "");
		text.setAttribute("stroke", "black");
		text.setTextContent(item.getName());
		g.appendChild(text);
	}

	private String cssColor(Color color) {
		return "#" + Integer.toHexString(color.getRGB());
	}

	private double calcLegendHeight() {
		// Max 75% of height
		double maxHeight = VIEWBOX_HEIGHT * 3 / 4;

		// Use item count + 1 multiplied by font height
		int itemCount = chart.getItemList().size();
		double height = LEGEND_FONT_SIZE * ((double) itemCount + 1);

		return height > maxHeight ? maxHeight : height;
	}

	private double calcLegendWidth() {
		// Max 20% of width
		double maxWidth = VIEWBOX_WIDTH / 5;

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

	private void createSectors() {
		Element svg = xmlDocument.getDocumentElement();

		Element g = xmlDocument.createElement("g");
		svg.appendChild(g);

		Point center = calcCenter();
		double radius = calcSectorRadius();

		System.err.println("Center: " + center);

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

	private double calcSectorRadius() {
		return (VIEWBOX_HEIGHT - calcHeaderHeight() - calcFooterHeight() - SECTOR_MARGIN * 2) / 2;
	}

	private int calcFooterHeight() {
		// TODO Auto-generated method stub
		return calcHeaderHeight();
	}

	private void drawSectorItem(Point center, double radius, double start,
			double end, Item item, Element g) {
		System.err.println(item.getName() + ": " + calcAngle(start) + " - "
				+ calcAngle(end) + " (" + calcPercentage(start, end) + "%)");

		String percentage = calcPercentage(start, end) + "%";
		Element path = xmlDocument.createElement("path");
		String pathData = calcPathData(center, start, end, radius, item
				.isHighlighted());
		path.setAttribute("d", pathData);
		path.setAttribute("fill", cssColor(item.getColor()));
		path.setAttribute("stroke", "black");
		path.setAttribute("title", percentage);

		g.appendChild(path);

	}

	private String calcPathData(Point center, double start, double end,
			double radius, boolean isHighlighted) {

		int offsetX = 0;
		int offsetY = 0;

		if (isHighlighted) {
			double middle = (end - start) / 2 + start;
			System.err.println("MiddleAngle:" + calcAngle(middle));
			offsetX = calcXFromFraction(middle, center, radius * 0.5)
					- center.x;
			offsetY = calcYFromFraction(middle, center, radius * 0.5)
					- center.y;
		}

		int centerX = center.x + offsetX;
		int centerY = center.y + offsetY;

		int startX = calcXFromFraction(start, center, radius) + offsetX;
		int startY = calcYFromFraction(start, center, radius) + offsetY;

		int endX = calcXFromFraction(end, center, radius) + offsetX;
		int endY = calcYFromFraction(end, center, radius) + offsetY;

		int xAxisOffset = 0; // calcAngle(start);

		String moveToCenter = "M" + centerX + "," + centerY;
		String lineToStart = " L" + startX + "," + startY;
		String arcToEnd = " A" + radius + "," + radius + " " + xAxisOffset
				+ " 0,1 " + endX + "," + endY;
		// String lineToEnd = " L" + endX + "," + endY;
		String closePath = " Z";

		return moveToCenter + lineToStart + arcToEnd + closePath;
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

	private Point calcCenter() {
		int x = VIEWBOX_WIDTH / 2; // - (int) calcLegendWidth();
		int y = VIEWBOX_HEIGHT / 2; // + calcHeaderHeight();
		return new Point(x, y);
	}

	private int calcHeaderHeight() {
		return HEADER_POSITION + HEADER_FONT_SIZE + HEADER_GUTTER;
	}

	private int calcAngle(double fraction) {
		return (int) Math.round(fraction * 360);
	}

	private String calcPercentage(double start, double end) {
		double fraction = Math.abs(start - end);
		double percentage = fraction * 100;
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
		nf.setMaximumFractionDigits(1);
		return nf.format(percentage);
	}

}
