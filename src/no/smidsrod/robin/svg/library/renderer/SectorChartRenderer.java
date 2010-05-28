package no.smidsrod.robin.svg.library.renderer;

import java.awt.Point;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import no.smidsrod.robin.svg.library.Chart;
import no.smidsrod.robin.svg.library.Item;

import org.w3c.dom.Element;

public class SectorChartRenderer extends AbstractSVGRenderer {

	private static final double HIGHLIGHTED_SECTOR_OFFSET_FACTOR = 0.25;

	private static final int SECTOR_LABEL_FONT_SIZE = 24;
	private static final double SMALL_SECTOR_THRESHOLD = 0.04;

	private static final double LARGE_SECTOR_OFFSET_FACTOR = 0.8;
	private static final double HIGHLIGHTED_LARGE_SECTOR_OFFSET_FACTOR = 0.85;

	private static final double SMALL_SECTOR_OFFSET_FACTOR = 1.2;
	private static final double HIGHLIGHTED_SMALL_SECTOR_OFFSET_FACTOR = 1.15;

	private Chart chart;

	public SectorChartRenderer(Chart chart) {
		super(chart);
		if (chart.getDimensionCount() < 1) {
			throw new IndexOutOfBoundsException(
					"The specified chart instance doesn't support at least 1 dimension");
		}
		this.chart = chart;
	}

	protected void buildSVGDocument() {
		ChartUtil.finalizeChart(chart);
		Element svg = SVGBuilder.createRootElement(getXMLDocument());
		SVGBuilder.createTitleAndDescription(svg, chart);
		Canvas.createBorderElement(svg);
		Header.createElement(svg, chart);
		Legend.createElement(svg, chart.getItemList());
		//DataRegion.createBorderElement(svg, chart.getItemList());
		createRangeLegend(svg);
		createTotalLegend(svg);
		createSectorElements(svg);
	}

	private void createRangeLegend(Element svg) {

		String rangeText = Axis.calcLabel(chart.getRange(0));

		if (rangeText.isEmpty()) {
			return;
		}

		// bottom/right
		int labelX = Canvas.WIDTH - Canvas.MARGIN;
		int labelY = Canvas.HEIGHT - Canvas.MARGIN;

		Element unitLegend = DOMBuilder.createElement(svg, "text");
		unitLegend.setAttribute("x", labelX + "");
		unitLegend.setAttribute("y", labelY + "");
		unitLegend.setAttribute("text-anchor", "end");
		unitLegend.setAttribute("font-family", "sans-serif");
		unitLegend.setAttribute("font-size", Footer.FONT_SIZE + "");
		unitLegend.setAttribute("stroke", "black");
		unitLegend.setTextContent(rangeText);

		svg.appendChild(unitLegend);

	}

	private void createTotalLegend(Element svg) {
		// bottom/left (two lines of text)
		int labelX = Canvas.MARGIN;
		int labelY = Canvas.HEIGHT - Canvas.MARGIN - Footer.FONT_SIZE;

		NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
		nf.setMaximumFractionDigits(2);

		double sumValues = calcSumValues(chart.getItemList());

		Element totalSum = DOMBuilder.createElement(svg, "text");
		totalSum.setAttribute("x", labelX + "");
		totalSum.setAttribute("y", labelY + "");
		totalSum.setAttribute("font-family", "sans-serif");
		totalSum.setAttribute("font-size", Footer.FONT_SIZE + "");
		totalSum.setAttribute("stroke", "black");
		totalSum.setTextContent("Total sum: " + nf.format(sumValues));

		svg.appendChild(totalSum);

		Element totalCount = DOMBuilder.createElement(svg, "text");
		totalCount.setAttribute("x", labelX + "");
		totalCount.setAttribute("y", labelY + Footer.FONT_SIZE + "");
		totalCount.setAttribute("font-family", "sans-serif");
		totalCount.setAttribute("font-size", Footer.FONT_SIZE + "");
		totalCount.setAttribute("stroke", "black");
		totalCount.setTextContent("Total items: " + chart.getItemList().size());

		svg.appendChild(totalCount);

	}

	private void createSectorElements(Element svg) {
		Element g = DOMBuilder.createElement(svg, "g");
		svg.appendChild(g);

		Point center = DataRegion.calcCenter(chart.getItemList());
		double radius = calcSectorRadius();

		List<Item> items = chart.getItemList();
		double sumValues = calcSumValues(items);
		double startValue = 0;
		for (Item item : items) {
			double currentValue = getValueForItem(item);
			double startPosition = calcPosition(startValue, sumValues);
			double endPosition = calcPosition(startValue + currentValue,
					sumValues);
			createSectorElement(g, center, radius, startPosition, endPosition,
					item);
			startValue += currentValue;
		}
	}

	private void createSectorElement(Element g, Point center, double radius,
			double start, double end, Item item) {

		String percentage = formatPercentage(start, end) + "%";

		String pathData = calcPathData(center, start, end, radius, item
				.isHighlighted());

		Element path = DOMBuilder.createElement(g, "path");
		path.setAttribute("d", pathData);
		path.setAttribute("fill", SVGUtil.cssColor(item.getColor()));
		path.setAttribute("stroke", "black");
		g.appendChild(path);

		double middle = calcMiddle(start, end);
		double labelRadius = calcSectorLabelRadius(radius, isSmallSector(start,
				end), item.isHighlighted());
		int labelX = calcXFromFraction(middle, center, labelRadius);
		int labelY = calcYFromFraction(middle, center, labelRadius);

		// Slight offset for Y based on font size
		int labelYOffset = SECTOR_LABEL_FONT_SIZE / 3;

		Element label = DOMBuilder.createElement(g, "text");
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

	private double calcSectorRadius() {
		int maxRadius = DataRegion.calcHeight() / 2;
		double actualMaxRadius = calcSectorLabelRadius((double) maxRadius, true, true);
		double factor = actualMaxRadius / maxRadius;
		return maxRadius / factor - ( SECTOR_LABEL_FONT_SIZE / 2 );
	}

	private boolean isSmallSector(double start, double end) {
		return Math.abs(start - end) < SMALL_SECTOR_THRESHOLD ? true : false;
	}

	private double calcSectorLabelRadius(double radius, boolean isSmallSector,
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

	private int calcAngle(double fraction) {
		return (int) Math.round(fraction * 360);
	}

	private double calcSumValues(List<Item> items) {
		double sumValues = 0;
		for (Item item : items) {
			sumValues += getValueForItem(item);
		}
		return sumValues;
	}

	private double getValueForItem(Item item) {
		if (item.getValueList().size() > 0) {
			// Always use the first dimension of the first Value instance
			return item.getValueList().get(0).get(chart.getRange(0));
		}
		throw new RuntimeException("There is no value for this item");
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
