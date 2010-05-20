package no.smidsrod.robin.svg.library;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;

/**
 * An interface for common functionality required by SVG renderer classes.
 * 
 * @author Robin Smidsrød <robin@smidsrod.no>
 * 
 */
public interface SVGRenderer {
	/**
	 * The XML namespace used for SVG documents.
	 * 
	 * See http://www.w3.org/TR/SVG11/attindex.html, xmlns attribute
	 * 
	 */
	static final String SVG_NAMESPACE = "http://www.w3.org/2000/svg";

	/**
	 * The XML namespace used for internal or external links in SVG documents.
	 * 
	 * See http://www.w3.org/TR/SVG11/attindex.html, xmlns:xlink attribute
	 */
	static final String XLINK_NAMESPACE = "http://www.w3.org/1999/xlink";

	/**
	 * The version of SVG we use. We use the latest released version of the
	 * specification as of April 2010.
	 */
	static final String SVG_VERSION = "1.1";

	/**
	 * @param outputStream
	 *            The specified OutputStream instance the SVG XML data should be
	 *            written to.
	 */
	public void renderSVGDocument(OutputStream outputStream);

	/**
	 * @return The SVG XML content is returned as one big String
	 */
	public String getSVGDocument();

	/**
	 * @param file
	 *            The File object specified will be truncated and the SVG XML
	 *            content will be written into the file.
	 * @throws FileNotFoundException
	 *             If the filename the File object points to is not writable for
	 *             some reason this exception will be thrown (see
	 *             FileOutputStream constructor for more info).
	 */
	public void storeSVGDocument(File file) throws FileNotFoundException;

	/**
	 * @param b
	 *            Output XML that is more human-readable if this is true.
	 *            Default should be false.
	 */
	public void setPrettyPrint(boolean b);
}
