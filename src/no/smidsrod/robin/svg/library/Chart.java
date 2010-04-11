package no.smidsrod.robin.svg.library;

import java.util.List;

public interface Chart {
	public int getDimensionCount();
	public Range[] getRanges();
	public Range getRange(int dimension);

	public List<Item> getItemList();

	public String getType();
	public void setTitle(String title);
	public String getTitle();
	public void setDescription(String description);
	public String getDescription();

	public SVGRenderer getSVGRenderer();
}
