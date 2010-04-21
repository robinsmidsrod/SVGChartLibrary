package no.smidsrod.robin.svg.library;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Robin Smidsrød <robin@smidsrod.no>
 *
 */
public abstract class AbstractChart implements Chart {

	/**
	 * Contains the main title for the chart
	 */
	private String title = "";

	/**
	 * Contains the main description for the chart
	 */
	private String description = "";

	/**
	 * Contains the list of ranges according to the dimension count
	 */
	private Range[] ranges;

	/**
	 * Contains the list of items in the chart
	 */
	private List<Item> itemList = new ArrayList<Item>();

	/**
	 * Instance initializer that should happen regardless of constructor used in
	 * subclass.
	 */
	{
		initRanges();
	}

	@Override
	abstract public int getDimensionCount();

	/**
	 * Initializes the ranges array with empty Range instances, according to
	 * dimension count.
	 */
	private void initRanges() {
		int dimensionCount = getDimensionCount();
		ranges = new Range[dimensionCount];
		for (int i = 0; i < dimensionCount; i++) {
			ranges[i] = new Range(i, getItemList());
		}
	}

	@Override
	public Range[] getRanges() {
		return ranges;
	}

	@Override
	public Range getRange(int dimension) {
		if (dimension < 0) {
			throw new IndexOutOfBoundsException("Dimension must be 0 or higher");
		}
		if (dimension > getDimensionCount() - 1) {
			throw new IndexOutOfBoundsException(
					"Dimension cannot be more than getDimensionCount() - 1");
		}
		return ranges[dimension];
	}

	@Override
	public List<Item> getItemList() {
		return itemList;
	}

	@Override
	abstract public String getType();

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	abstract public SVGRenderer getSVGRenderer();

}