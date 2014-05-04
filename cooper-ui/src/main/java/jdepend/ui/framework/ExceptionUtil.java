package jdepend.ui.framework;

public final class ExceptionUtil {

	public static String getMessage(Throwable e) {
		while (e.getCause() != null) {
			e = e.getCause();
		}
		return e.getMessage();
	}

}
