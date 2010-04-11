package no.smidsrod.robin.svg.library;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

/**
 * @author Robin Smidsrød <robin@smidsrod.no>
 *
 */
public abstract class AbstractSVGRenderer implements SVGRenderer {

	@Override
	abstract public void renderSVG(OutputStream outputStream);

	@Override
	public String getSVG() {
		ByteOutputStream out = new ByteOutputStream();
		renderSVG(out);
		return out.toString();
	}

	@Override
	public void storeSVG(File file) throws FileNotFoundException {
		FileOutputStream out = new FileOutputStream(file);
		renderSVG(out);
	}

}
