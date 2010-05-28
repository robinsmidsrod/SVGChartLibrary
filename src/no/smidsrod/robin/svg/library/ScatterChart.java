package no.smidsrod.robin.svg.library;

import java.util.List;

import no.smidsrod.robin.svg.library.renderer.SVGRenderer;
import no.smidsrod.robin.svg.library.renderer.ScatterChartRenderer;

/**
 * Implementation of a scatter chart.
 * <p>
 * Supports three dimensions and multiple values per item in the current
 * implementation. The third dimension is the radius of each circle.
 * 
 * @author Robin Smidsrød <robin@smidsrod.no>
 * 
 */
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
