package no.smidsrod.robin.svg.library.renderer;

import java.awt.Color;
import java.awt.Point;
import java.util.List;

import no.smidsrod.robin.svg.library.Chart;
import no.smidsrod.robin.svg.library.Item;
import no.smidsrod.robin.svg.library.Range;
import no.smidsrod.robin.svg.library.Value;

import org.w3c.dom.Element;

public class ScatterChartRenderer extends AbstractSVGRenderer implements
		SVGRenderer {

	private Chart chart;

	public ScatterChartRenderer(Chart chart) {
		super(chart);
		if (chart.getDimensionCount() < 3) {
			throw new IndexOutOfBoundsException(
					"The specified chart instance doesn't support at least 3 dimensions");
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
		Range zRange = chart.getRange(2); // range 2 is circle radius
		Axis.createHorizontalElement(svg, xRange, chart.getItemList());
		Axis.createVerticalElement(svg, yRange, chart.getItemList());
		createCircleElements(svg, xRange, yRange, zRange, chart.getItemList());
	}

	private void createCircleElements(Element svg, Range xRange, Range yRange,
			Range zRange, List<Item> items) {

		SVGBuilder.createHighlightFilterElement(svg);
		DataRegion.createClipElement(svg, items);

		// Created clipped container
		Element container = DOMBuilder.createElement(svg, "g");
		container.setAttribute("clip-path", "url(#dataRegion)");
		svg.appendChild(container);

		double xScaleFactor = DataRegion.calcWidth(items)
				/ xRange.calcTotalDistance();
		double yScaleFactor = DataRegion.calcHeight()
				/ yRange.calcTotalDistance();
		// Max radius is one sixth of the data region's height
		double zScaleFactor = (DataRegion.calcHeight() / 6)
				/ zRange.calcTotalDistance();

		for (Item item : items) {
			Color color = item.getColor();
			boolean highlighted = item.isHighlighted();
			List<Value> values = item.getValueList();
			createCircleElement(container, color, highlighted, values, xRange,
					yRange, zRange, xScaleFactor, yScaleFactor, zScaleFactor);
		}

	}

	private void createCircleElement(Element svg, Color color,
			boolean isHighlighted, List<Value> values, Range xRange,
			Range yRange, Range zRange, double xScaleFactor,
			double yScaleFactor, double zScaleFactor) {

		for (Value value : values) {
			Point center = DataRegion.calcPoint(value, xRange, yRange,
					xScaleFactor, yScaleFactor);
			double radius = value.get(zRange) * zScaleFactor;
			String filter = isHighlighted ? "url(#highlight)" : "";
			SVGBuilder.createCircleElement(svg, center, radius, SVGUtil
					.cssColor(color), filter);
		}

	}

}
