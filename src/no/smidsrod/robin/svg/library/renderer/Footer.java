package no.smidsrod.robin.svg.library.renderer;

class Footer {

	static final int FONT_SIZE = 18;

	static final int LINE_COUNT = 2;

	static int calcHeight() {
		return Canvas.MARGIN + FONT_SIZE * LINE_COUNT;
	}

}
