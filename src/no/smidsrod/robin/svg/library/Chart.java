package no.smidsrod.robin.svg.library;

import java.util.List;

/**
 * @author Robin Smidsrød <robin@smidsrod.no>
 * 
 */
public interface Chart {
	/**
	 * @return number of dimensions this chart can represent
	 */
	public int getDimensionCount();

	/**
	 * @return An array of Range instances according to the number of dimensions
	 *         present
	 */
	public Range[] getRanges();

	/**
	 * @param dimension
	 * @return The specified Range instance for the specified dimension
	 */
	public Range getRange(int dimension);

	/**
	 * @return A list of Item instances that represents the data elements of the
	 *         chart
	 */
	public List<Item> getItemList();

	/**
	 * @return The type of chart, static identifier used for serialization of
	 *         data structure
	 */
	public String getType();

	/**
	 * @param title
	 *            Main title of chart
	 */
	public void setTitle(String title);

	/**
	 * @return Main title of chart
	 */
	public String getTitle();

	/**
	 * @param description
	 *            Main description of chart
	 */
	public void setDescription(String description);

	/**
	 * @return Main description of chart
	 */
	public String getDescription();

	/**
	 * @return An instance of the SVGRenderer interface, which is used to
	 *         actually write the SVG code to an OutputStream, String or File
	 *         object.
	 */
	public SVGRenderer getSVGRenderer();

}
