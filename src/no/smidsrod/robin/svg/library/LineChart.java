package no.smidsrod.robin.svg.library;

import java.util.List;

/**
 * Implementation of a typical line chart.
 * <p>
 * Supports two dimensions and multiple values per item in the current
 * implementation.
 * 
 * @author Robin Smidsrød <robin@smidsrod.no>
 * 
 */
public class LineChart extends AbstractChart {

	public LineChart() {
		super();
	}

	public LineChart(String title, String description) {
		super(title, description);
	}

	public LineChart(String title, String description, List<Item> itemList) {
		super(title, description, itemList);
	}

	@Override
	public int getDimensionCount() {
		return 2;
	}

	@Override
	public String getType() {
		return "line";
	}

	@Override
	public SVGRenderer getSVGRenderer() {
		return new LineChartRenderer(this);
	}


}
