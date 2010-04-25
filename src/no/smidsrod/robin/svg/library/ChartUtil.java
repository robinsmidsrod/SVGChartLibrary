package no.smidsrod.robin.svg.library;

import java.awt.Color;
import java.util.List;

class ChartUtil {

	/**
	 * Do tasks that is required on a chart before the SVG document is
	 * generated.
	 * 
	 * @param chart
	 *            The chart to manipulate.
	 */
	static void finalizeChart(Chart chart) {
		calcItemColors(chart);
	}

	/**
	 * Calculate colors with good contrast between each other for all the items
	 * in the chart that does not have any color set.
	 * 
	 * @param chart
	 *            A chart with items that need colorization.
	 */
	private static void calcItemColors(Chart chart) {
		List<Item> items = chart.getItemList();

		// Figure out how many items that need colors calculated
		int numberOfItems = 0;
		for (Item item : items) {
			if (item.getColor() == Item.DEFAULT_COLOR) {
				numberOfItems++;
			}
		}

		// Just stop if no items needs color calculated
		if (numberOfItems == 0) {
			return;
		}

		// Set color on the items that needs it
		int i = 0;
		for (Item item : items) {
			if (item.getColor() == Item.DEFAULT_COLOR) {
				float interval = 1f / numberOfItems;
				float hue = interval * (float) i;
				item.setColor(new Color(Color.HSBtoRGB(hue, 1f, 1f)));
				i++;
			}
		}

	}

}
