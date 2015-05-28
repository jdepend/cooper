package jdepend.framework.ui.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import jdepend.framework.ui.component.TextViewer;

public final class ExceptionPrinter {

	public static JComponent createComponent(Exception e) {
		StringBuilder error = new StringBuilder(100);

		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		error.append(sw.toString());

		TextViewer errorText = new TextViewer();
		errorText.setText(error.toString());

		return new JScrollPane(errorText);
	}

}
