package no.smidsrod.robin.svg.library;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.w3c.dom.Document;

/**
 * @author Robin Smidsrød <robin@smidsrod.no>
 * 
 */
public abstract class AbstractSVGRenderer implements SVGRenderer {

	private Document xmlDocument;

	private boolean prettyPrint = false;

	public AbstractSVGRenderer(Chart chart) {
		if (chart == null) {
			throw new NullPointerException(
					"Please specify an instance of the Chart interface");
		}
	}

	@Override
	public void renderSVGDocument(OutputStream outputStream) {
		if (xmlDocument == null) {
			initXMLDocument();
			buildSVGDocument();
		}
		XMLSerializer serializer = new XMLSerializer(outputStream);
		serializer.setPrettyPrint(getPrettyPrint());
		serializer.write(getXMLDocument());
	}

	/**
	 * Implement this method by adding elements to the XML Document returned by
	 * getXMLDocument() to form the complete SVG document.
	 */
	abstract void buildSVGDocument();

	@Override
	public String getSVGDocument() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		renderSVGDocument(out);
		return out.toString();
	}

	@Override
	public void storeSVGDocument(File file) throws FileNotFoundException {
		FileOutputStream out = new FileOutputStream(file);
		renderSVGDocument(out);
	}

	/**
	 * @return The value of the prettyPrint flag.
	 */
	public boolean getPrettyPrint() {
		return prettyPrint;
	}

	@Override
	public void setPrettyPrint(boolean prettyPrint) {
		this.prettyPrint = prettyPrint;
	}

	private void initXMLDocument() {
		xmlDocument = DOMBuilder.newDocument(); // Create new DOM object
	}

	/**
	 * If there is no instance available, a new empty instance will be created.
	 * 
	 * @return A standard XML document instance
	 */
	public Document getXMLDocument() {
		if (xmlDocument == null) {
			initXMLDocument();
		}
		return xmlDocument;
	}

	/**
	 * Will reset the entire XML document so that buildSVGDocument() will be
	 * called again whenever one of the serialization methods are used.
	 */
	public void invalidate() {
		xmlDocument = null;
	}

}
