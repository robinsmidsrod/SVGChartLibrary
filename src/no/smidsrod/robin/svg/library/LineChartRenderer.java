package no.smidsrod.robin.svg.library;

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
		//DataRegion.createBorderElement(svg, chart.getItemList());
		Axis.createVerticalElement(svg, chart);

		// createHorizontalAxis();
		// createLines();
	}

}
