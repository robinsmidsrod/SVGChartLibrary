package no.smidsrod.robin.svg.library.renderer;

import java.io.OutputStream;

import org.w3c.dom.Document;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

/**
 * @author Robin Smidsrød <robin@smidsrod.no>
 *
 *         Using Load&Save DOM XML serializer from
 *         http://xerces.apache.org/xerces2-j/faq-dom.html
 */
class XMLSerializer {

	private LSOutput destination;
	private boolean prettyPrint = false;

	void setPrettyPrint(boolean prettyPrint) {
		this.prettyPrint = prettyPrint;
	}

	XMLSerializer(OutputStream outputStream) {
		destination = createLSOutput();
		destination.setByteStream(outputStream);
		destination.setEncoding("utf-8");
	}

	void write(Document xmlDocument) {
		LSSerializer serializer = getDOMSerializer();
		serializer.getDomConfig().setParameter("format-pretty-print",
				prettyPrint);
		serializer.write(xmlDocument, destination);
	}

	private LSSerializer getDOMSerializer() {
		try {
			DOMImplementationRegistry registry = DOMImplementationRegistry
					.newInstance();
			DOMImplementationLS impl = (DOMImplementationLS) registry
					.getDOMImplementation("LS");
			return impl.createLSSerializer();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private LSOutput createLSOutput() {
		try {
			DOMImplementationRegistry registry = DOMImplementationRegistry
					.newInstance();
			DOMImplementationLS impl = (DOMImplementationLS) registry
					.getDOMImplementation("LS");
			return impl.createLSOutput();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
