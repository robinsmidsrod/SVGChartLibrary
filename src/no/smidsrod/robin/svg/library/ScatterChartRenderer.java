package no.smidsrod.robin.svg.library;

import org.w3c.dom.Element;

public class ScatterChartRenderer extends AbstractSVGRenderer implements
		SVGRenderer {

	private Chart chart;

	public ScatterChartRenderer(Chart chart) {
		if (chart == null) {
			throw new NullPointerException(
					"Please specify an instance of the Chart interface");
		}
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
		DataRegion.createBorderElement(svg, chart.getItemList());

		// createHorizontalAxis();
		// createVerticalAxis();
		// createCircles();
	}

}
