package no.smidsrod.robin.svg.library.renderer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

class DOMBuilder {

	/**
	 * Taken from the Java JAXP DOM tutorial.
	 * 
	 * http://java.sun.com/j2ee/1.4/docs/tutorial/doc/JAXPDOM7.html#wp65002
	 * 
	 * @return Blank document object.
	 */
	static Document newDocument() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document doc = builder.newDocument();
			doc.setXmlVersion("1.0");
			return doc;
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Creates a new Element with the given name.
	 * 
	 * @param element
	 *            any type of Element attached to a Document
	 * @param name
	 *            the name of the new element
	 * @return the created element instance
	 */
	static Element createElement(Element element, String name) {
		return element.getOwnerDocument().createElement(name);
	}

}
