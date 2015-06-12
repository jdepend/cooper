package jdepend.client.report.way.textui;

import java.io.OutputStream;
import java.io.PrintWriter;

public abstract class Printer {

	private PrintWriter writer;

	private OutputStream stream;

	/**
	 * Sets the output stream.
	 * 
	 * @param stream
	 *            Output stream.
	 */
	public void setStream(OutputStream stream) {
		this.stream = stream;
		this.writer = new PrintWriter(this.stream);

	}

	public OutputStream getStream() {
		return stream;
	}

	public PrintWriter getWriter() {
		return this.writer;
	}

}
