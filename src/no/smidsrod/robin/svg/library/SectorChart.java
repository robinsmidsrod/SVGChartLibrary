package no.smidsrod.robin.svg.library;

import java.util.List;

/**
 * Implementation of a typical sector/pie chart.
 * <p>
 * Only supports one dimension and one value per item in the current
 * implementation.
 * 
 * @author Robin Smidsrød <robin@smidsrod.no>
 * 
 */
public class SectorChart extends AbstractChart {

	public SectorChart() {
		super();
	}

	public SectorChart(String title, String description) {
		super(title, description);
	}

	public SectorChart(String title, String description, List<Item> itemList) {
		super(title, description, itemList);
	}

	@Override
	public int getDimensionCount() {
		return 1;
	}

	@Override
	public String getType() {
		return "sector";
	}

	@Override
	public SVGRenderer getSVGRenderer() {
		return new SectorChartRenderer(this);
	}

}
