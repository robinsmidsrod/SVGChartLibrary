package no.smidsrod.robin.svg.library.renderer;

import java.awt.Color;
import java.util.List;

import no.smidsrod.robin.svg.library.Chart;
import no.smidsrod.robin.svg.library.Item;

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

		// Random random = new Random();

		// Set color on the items that needs it
		int i = 0;
		for (Item item : items) {
			if (item.getColor() == Item.DEFAULT_COLOR) {
				// FIXME: Not a very good algorithm
				float interval = 1f / numberOfItems;
				float hue = interval * (float) i;
				float sat = 1f;
				float brightness = 1f;
				// float hue = random.nextFloat();
				// float sat = random.nextFloat();
				// float brightness = random.nextFloat();
				item.setColor(new Color(Color.HSBtoRGB(hue, sat, brightness)));
				i++;
			}
		}

	}

}
