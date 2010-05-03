package no.smidsrod.robin.svg.library;

import java.awt.Point;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import org.w3c.dom.Element;

public class BarChartRenderer extends AbstractSVGRenderer implements
		SVGRenderer {

	private static final int BAR_MARGIN = 5;
	private Chart chart;

	public BarChartRenderer(Chart chart) {
		super(chart);
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
		Axis.createVerticalElement(svg, chart.getRange(0), chart.getItemList());
		createHorizontalLine(svg, chart.getItemList());
		createBarElements(svg, chart.getRange(0), chart.getItemList());
	}

	private void createHorizontalLine(Element svg, List<Item> items) {
		int startX = DataRegion.calcLeft();
		int endX = DataRegion.calcRight(items);
		int lineY = DataRegion.calcBottom();
		Point start = new Point(startX, lineY);
		Point end = new Point(endX, lineY);
		SVGBuilder.createLineElement(svg, start, end, "black");
	}

	private void createBarElements(Element svg, Range range, List<Item> items) {
		if (items.size() == 0) {
			return;
		}

		// Pattern used for highlighting
		createDiagonalPatternElement(svg);

		double widthPerItem = DataRegion.calcWidth(items) / (items.size() + 1);
		double scaleFactor = DataRegion.calcHeight()
				/ range.calcTotalDistance();
		int counter = 1;
		for (Item item : items) {
			createBarElement(svg, item, range, widthPerItem, scaleFactor,
					counter);
			counter++;
		}
	}

	private void createBarElement(Element svg, Item item, Range range,
			double widthPerItem, double scaleFactor, int itemNumber) {

		int x = calcBarLeft(itemNumber, widthPerItem);
		int y = calcBarTop(item, range, scaleFactor);
		int width = calcBarWidth(widthPerItem);
		int height = calcBarHeight(item, range, scaleFactor);

		Element bar = DOMBuilder.createElement(svg, "rect");
		bar.setAttribute("x", x + "");
		bar.setAttribute("y", y + "");
		bar.setAttribute("width", width + "");
		bar.setAttribute("height", height + "");
		bar.setAttribute("stroke", "black");
		bar.setAttribute("fill", SVGUtil.cssColor(item.getColor()));
		svg.appendChild(bar);

		if (item.isHighlighted()) {
			Element highlight = DOMBuilder.createElement(svg, "rect");
			highlight.setAttribute("x", x + "");
			highlight.setAttribute("y", y + "");
			highlight.setAttribute("width", width + "");
			highlight.setAttribute("height", height + "");
			highlight.setAttribute("stroke", "black");
			highlight.setAttribute("fill", "url(#diagonalPattern)");
			svg.appendChild(highlight);
		}

		Point position = new Point(x, DataRegion.calcBottom());
		int fontSize = 12;
		int xOffset = fontSize / 6;
		int yOffset = -fontSize / 6;
		String transform = "rotate(90 " + position.x + "," + position.y + ")";
		String text = formatValue(item.getValueList().get(0).get(range));

		Element label = DOMBuilder.createElement(svg, "text");
		label.setAttribute("x", position.x + xOffset + "");
		label.setAttribute("y", position.y + yOffset + "");
		label.setAttribute("stroke", "black");
		label.setAttribute("text-anchor", "start");
		label.setAttribute("font-family", "sans-serif");
		label.setAttribute("font-size", fontSize + "");
		label.setAttribute("transform", transform);
		label.setTextContent(text);
		svg.appendChild(label);
	}

	private void createDiagonalPatternElement(Element svg) {
		Element defs = DOMBuilder.createElement(svg, "defs");
		svg.appendChild(defs);

		Element pattern = DOMBuilder.createElement(defs, "pattern");
		pattern.setAttribute("id", "diagonalPattern");
		pattern.setAttribute("patternUnits", "userSpaceOnUse");
		pattern.setAttribute("viewBox", "0 0 10 10");
		pattern.setAttribute("x", "0");
		pattern.setAttribute("y", "0");
		pattern.setAttribute("width", "10");
		pattern.setAttribute("height", "10");
		defs.appendChild(pattern);

		SVGBuilder.createLineElement(pattern, new Point(0, 0),
				new Point(10, 10), "black");
	}

	private int calcBarLeft(int itemNumber, double widthPerItem) {
		double left = DataRegion.calcLeft() + widthPerItem * itemNumber
				+ BAR_MARGIN;
		return (int) left;
	}

	private int calcBarTop(Item item, Range range, double scaleFactor) {
		return (int) (DataRegion.calcBottom() - calcBarHeight(item, range,
				scaleFactor));
	}

	private int calcBarWidth(double widthPerItem) {
		double width = widthPerItem - BAR_MARGIN * 2;
		return (int) width;
	}

	private int calcBarHeight(Item item, Range range, double scaleFactor) {
		double value = item.getValueList().get(0).get(range);
		double valueInPixels = value * scaleFactor;
		return (int) valueInPixels;
	}

	private String formatValue(double value) {
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
		nf.setMaximumFractionDigits(2);
		String labelText = nf.format(value);
		return labelText;
	}

}
