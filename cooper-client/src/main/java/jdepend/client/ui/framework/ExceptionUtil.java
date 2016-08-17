package jdepend.client.ui.framework;

public final class ExceptionUtil {

	public static String getMessage(Throwable e) {
		while (e.getMessage() == null && e.getCause() != null) {
			e = e.getCause();
		}
		return e.getMessage();
	}

}
