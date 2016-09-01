package jdepend.client.ui.util;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class ClassPathURLStreamHandler extends URLStreamHandler {

	@Override
	protected URLConnection openConnection(URL u) throws IOException {
		final URL resourceUrl = ClassPathURLStreamHandler.class.getResource(u.getPath());
        return resourceUrl.openConnection();
	}

}
