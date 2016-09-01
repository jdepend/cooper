package jdepend.framework.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import jdepend.framework.exception.JDependException;

public final class StreamUtil {

	public static byte[] getData(InputStream inputStream) throws JDependException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		byte[] buffer = new byte[64 * 1024];
		int count;
		try {
			for (;;) {
				count = inputStream.read(buffer);
				if (count < 0)
					break;
				os.write(buffer, 0, count);
			}
			os.flush();
			return os.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			throw new JDependException(e);
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
