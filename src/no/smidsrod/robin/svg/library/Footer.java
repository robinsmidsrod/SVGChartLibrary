package no.smidsrod.robin.svg.library;

class Footer {

	static final int FONT_SIZE = 18;

	static int calcHeight(int lineCount) {
		return Canvas.MARGIN + FONT_SIZE * lineCount;
	}

}
