package no.smidsrod.robin.svg.library;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractChart implements Chart {

	private String title = "";
	private String description = "";
	private Range[] ranges;
	private List<Item> itemList;

	/* Initializer */
	{
		initRanges();
		initItemList();
	}

	@Override
	abstract public int getDimensionCount();

	private void initRanges() {
		int dimensionCount = getDimensionCount();
		ranges = new Range[dimensionCount];
		for(int i=0; i < dimensionCount; i++) {
			ranges[i] = new Range();
		}
	}

	@Override
	public Range[] getRanges() {
		return ranges;
	}

	@Override
	public Range getRange(int dimension) {
		if ( dimension < 0 ) {
			throw new IndexOutOfBoundsException("Dimension must be 0 or higher");
		}
		if ( dimension > getDimensionCount() - 1) {
			throw new IndexOutOfBoundsException("Dimension cannot be more than getDimensionCount() - 1");
		}
		return ranges[dimension];
	}

	private void initItemList() {
		itemList = new ArrayList<Item>();
	}

	@Override
	public List<Item> getItemList() {
		return itemList;
	}

	@Override
	abstract public String getType();

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String getTitle() {
		return title;
	}

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
