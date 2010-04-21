package no.smidsrod.robin.svg.library;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;

/**
 * @author Robin Smidsrød <robin@smidsrod.no>
 *
 */
public interface SVGRenderer {
	/**
	 * @param outputStream
	 *            The specified OutputStream instance the SVG XML data should be
	 *            written to.
	 */
	public void renderSVG(OutputStream outputStream);

	/**
	 * @return The SVG XML content is returned as one big String
	 */
	public String getSVG();

	/**
	 * @param file
	 *            The File object specified will be truncated and the SVG XML
	 *            content will be written into the file.
	 * @throws FileNotFoundException
	 *             If the filename the File object points to is not writable for
	 *             some reason this exception will be thrown (see
	 *             FileOutputStream constructor for more info).
	 */
	public void storeSVG(File file) throws FileNotFoundException;

	/**
	 * @param b
	 *            Output XML that is more human-readable if this is true.
	 *            Default should be false.
	 */
	public void setPrettyPrint(boolean b);
}
