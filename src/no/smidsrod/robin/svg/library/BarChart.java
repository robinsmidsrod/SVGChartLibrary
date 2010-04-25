package no.smidsrod.robin.svg.library;

import java.util.List;

public class BarChart extends AbstractChart {

	public BarChart() {
		super();
	}

	public BarChart(String title, String description) {
		super(title, description);
	}

	public BarChart(String title, String description, List<Item> itemList) {
		super(title, description, itemList);
	}

	@Override
	public int getDimensionCount() {
		return 1;
	}

	@Override
	public String getType() {
		return "bar";
	}

	@Override
	public SVGRenderer getSVGRenderer() {
		return new BarChartRenderer(this);
	}

}
