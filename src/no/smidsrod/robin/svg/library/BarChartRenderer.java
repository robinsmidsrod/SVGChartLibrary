package no.smidsrod.robin.svg.library;

import java.awt.Point;
import java.util.List;

import org.w3c.dom.Element;

public class BarChartRenderer extends AbstractSVGRenderer implements
		SVGRenderer {

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

		Axis.createVerticalElement(svg, chart);
		createHorizontalLine(svg, chart.getItemList());
		// createBars();
	}

	private void createHorizontalLine(Element svg, List<Item> items) {
		int startX = DataRegion.calcLeft();
		int endX = DataRegion.calcRight(items);
		int lineY = DataRegion.calcBottom();
		Point start = new Point(startX, lineY);
		Point end = new Point(endX, lineY);
		SVGBuilder.createLineElement(svg, start, end, "black");
	}

}
