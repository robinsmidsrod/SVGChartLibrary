package no.smidsrod.robin.svg.library;

import java.util.List;

public class ScatterChart extends AbstractChart {

	public ScatterChart() {
		super();
	}

	public ScatterChart(String title, String description) {
		super(title, description);
	}

	public ScatterChart(String title, String description, List<Item> itemList) {
		super(title, description, itemList);
	}

	@Override
	public int getDimensionCount() {
		return 3;
	}

	@Override
	public String getType() {
		return "scatter";
	}

	@Override
	public SVGRenderer getSVGRenderer() {
		return new ScatterChartRenderer(this);
	}

}
