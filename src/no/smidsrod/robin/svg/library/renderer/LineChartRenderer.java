package no.smidsrod.robin.svg.library.renderer;

import java.awt.Color;
import java.awt.Point;
import java.util.List;

import no.smidsrod.robin.svg.library.Chart;
import no.smidsrod.robin.svg.library.Item;
import no.smidsrod.robin.svg.library.Range;
import no.smidsrod.robin.svg.library.Value;

import org.w3c.dom.Element;

public class LineChartRenderer extends AbstractSVGRenderer implements
		SVGRenderer {

	private Chart chart;

	public LineChartRenderer(Chart chart) {
		super(chart);
		if (chart.getDimensionCount() < 2) {
			throw new IndexOutOfBoundsException(
					"The specified chart instance doesn't support at least 2 dimensions");
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
		Range xRange = chart.getRange(0); // range 0 is X axis
		Range yRange = chart.getRange(1); // range 1 is Y axis
		Axis.createHorizontalElement(svg, xRange, chart.getItemList());
		Axis.createVerticalElement(svg, yRange, chart.getItemList());
		createLineElements(svg, xRange, yRange, chart.getItemList());
	}

	private void createLineElements(Element svg, Range xRange, Range yRange,
			List<Item> items) {
		double xScaleFactor = DataRegion.calcWidth(items)
				/ xRange.calcTotalDistance();
		double yScaleFactor = DataRegion.calcHeight()
				/ yRange.calcTotalDistance();
		SVGBuilder.createHighlightFilterElement(svg);
		for (Item item : items) {
			createPolyLineElement(svg, item.getColor(), item.isHighlighted(),
					item.getValueList(), xRange, yRange, xScaleFactor,
					yScaleFactor);
		}
	}

	private void createPolyLineElement(Element svg, Color color,
			boolean isHighlighted, List<Value> values, Range xRange,
			Range yRange, double xScaleFactor, double yScaleFactor) {
		Point start = null;
		for (Value value : values) {
			Point end = DataRegion.calcPoint(value, xRange, yRange,
					xScaleFactor, yScaleFactor);
			if (start == null) {
				start = new Point(end);
			}
			String filter = isHighlighted ? "url(#highlight)" : "";
			SVGBuilder.createDotElement(svg, start, SVGUtil.cssColor(color),
					filter);
			SVGBuilder.createLineElement(svg, start, end, SVGUtil
					.cssColor(color), filter);
			SVGBuilder.createDotElement(svg, end, SVGUtil.cssColor(color),
					filter);
			start = new Point(end);
		}

	}

}
