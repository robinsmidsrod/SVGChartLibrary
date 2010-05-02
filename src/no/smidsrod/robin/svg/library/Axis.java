package no.smidsrod.robin.svg.library;

class Axis {

	/**
	 * @param range
	 *            The range you want to calculate the label for.
	 * @return The calculated label.
	 */
	static String calcLabel(Range range) {

		// Nothing specified, return empty string
		if (range.getName().isEmpty() && range.getUnit().isEmpty()) {
			return "";
		}

		String rangeText = "";

		if (!range.getName().isEmpty()) {
			rangeText += range.getName();
		}

		if (!range.getUnit().isEmpty()) {
			rangeText += " (" + range.getUnit() + ")";
		}

		return rangeText;
	}

}
