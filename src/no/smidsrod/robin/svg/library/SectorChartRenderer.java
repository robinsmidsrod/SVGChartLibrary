package no.smidsrod.robin.svg.library;

import java.awt.Color;
import java.io.OutputStream;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SectorChartRenderer extends AbstractSVGRenderer {

	// Traditional 4:3 aspect (landscape)
	private static final int VIEWBOX_WIDTH = 1000;
	private static final int VIEWBOX_HEIGHT = 750;

	private static final String SVG_NAMESPACE = "http://www.w3.org/2000/svg";
	private static final String SVG_VERSION = "1.1";

	private Chart chart;
	private Document xmlDocument;
	private boolean prettyPrint = false;
	private double legendFontHeight = 24;

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
		text.setAttribute("y", "50");
		text.setAttribute("font-family", "sans-serif");
		text.setAttribute("font-size", "32");
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
		double y = (counter + 1) * legendFontHeight + (legendFontHeight / 3);

		Element circle = xmlDocument.createElement("circle");
		circle.setAttribute("cx", "15");
		circle.setAttribute("cy", (y - 9) + "");
		circle.setAttribute("r", (legendFontHeight * 0.4) + "");
		circle.setAttribute("fill", cssColor(item.getColor()));
		g.appendChild(circle);

		Element text = xmlDocument.createElement("text");
		text.setAttribute("x", (legendFontHeight * 1.25) + "");
		text.setAttribute("y", y + "");
		text.setAttribute("font-family", "sans-serif");
		text.setAttribute("font-size", legendFontHeight + "");
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
		double height = legendFontHeight * ((double) itemCount + 1);

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

		// Sans-serif fonts have approx. 50% width compared to height (estimated)
		double circleWidth = 15 + legendFontHeight * 0.4 * 2;
		double width = legendFontHeight * 0.50 * maxLength + circleWidth + 10;

		return width > maxWidth ? maxWidth : width;
	}

	private void createSectors() {
		Element svg = xmlDocument.getDocumentElement();

		Element g = xmlDocument.createElement("g");
		svg.appendChild(g);

		List<Item> items = chart.getItemList();
		double sumValues = calcSumValues(items);
		for(Item item: items) {
			drawSectorItem(sumValues, item, g);
		}
	}

	private double calcSumValues(List<Item> items) {
		double sumValues = 0;
		for(Item item: items) {
			if ( item.getValueList().size() > 0 ) {
				sumValues += item.getValueList().get(0).get(0);
			}
		}
		return sumValues;
	}

	private void drawSectorItem(double sumValues, Item item, Element g) {
		double currentValue = 0;
		if ( item.getValueList().size() > 0 ) {
			currentValue = item.getValueList().get(0).get(0);
		}
		System.err.println(item.getName() + ": " + currentValue + " - "
				+ sumValues + " (" + (currentValue / sumValues ) + ")");
		// TODO
	}

}
