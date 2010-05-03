package no.smidsrod.robin.svg.library;

import java.util.List;

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
		// TODO Auto-generated method stub

	}

}
