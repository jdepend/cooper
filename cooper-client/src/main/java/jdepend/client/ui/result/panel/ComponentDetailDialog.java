package jdepend.client.ui.result.panel;

import java.awt.BorderLayout;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JScrollPane;

import jdepend.client.report.way.textui.TextSummaryPrinter;
import jdepend.framework.ui.component.TextViewer;
import jdepend.framework.ui.dialog.CooperDialog;
import jdepend.model.Component;

public final class ComponentDetailDialog extends CooperDialog {

	private String detailText = null;

	public ComponentDetailDialog(Component component) {

		super(component.getName());

		getContentPane().setLayout(new BorderLayout());

		TextViewer classProperty = new TextViewer();

		printComponent(component);

		classProperty.setText(detailText);
		classProperty.setCaretPosition(0);

		this.add(new JScrollPane(classProperty));
	}

	private void printComponent(Component component) {

		OutputStream info = new ByteArrayOutputStream();

		TextSummaryPrinter printer = new TextSummaryPrinter();

		printer.setStream(info);

		printer.printComponent(component);

		printer.getWriter().flush();

		detailText = info.toString();

		try {
			info.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
