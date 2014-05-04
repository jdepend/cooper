package org.wilmascope.dotparser;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Creates a filter stream which removes linebreaks (dos or unix) escaped with a
 * back-slash, concatenating the lines together, ie:
 * 
 * blah blah blah\<lf> (or <cr><lf>) bluk bluk bluk
 * 
 * becomes:
 * 
 * blah blah blahbluk bluk bluk
 */
public class LineBreakFilter extends FilterInputStream {
	public LineBreakFilter(InputStream stream) {
		super(stream);
	}

	public int read() throws IOException {
		int c = super.read();
		if (c == '\\') {
			mark(2);
			int d = super.read();
			if (d == '\n') {
				c = super.read();
			} else if (d == '\r') {
				c = super.read();
				c = super.read();
			} else {
				reset();
			}
		}
		return c;
	}

	public int read(byte b[], int off, int len) throws IOException {
		if (b == null) {
			throw new NullPointerException();
		} else if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) > b.length) || ((off + len) < 0)) {
			throw new IndexOutOfBoundsException();
		} else if (len == 0) {
			return 0;
		}

		int c = read();
		if (c == -1) {
			return -1;
		}
		b[off] = (byte) c;

		int i = 1;
		try {
			for (; i < len; i++) {
				c = read();
				if (c == -1) {
					break;
				}
				if (b != null) {
					b[off + i] = (byte) c;
				}
			}
		} catch (IOException ee) {
		}
		return i;
	}
}
