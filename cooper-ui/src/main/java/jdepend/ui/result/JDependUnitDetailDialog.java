package jdepend.ui.result;

import java.awt.BorderLayout;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JScrollPane;

import jdepend.framework.ui.CooperDialog;
import jdepend.framework.ui.TextViewer;
import jdepend.model.JDependUnit;
import jdepend.model.JDependUnitMgr;
import jdepend.report.way.textui.TextSummaryPrinter;

public final class JDependUnitDetailDialog extends CooperDialog {

	private String detailText = null;

	public JDependUnitDetailDialog(String name) {

		super(name);

		getContentPane().setLayout(new BorderLayout());

		TextViewer classProperty = new TextViewer();

		JDependUnit unit = JDependUnitMgr.getInstance().getUnit(name);

		printUnit(unit);

		classProperty.setText(detailText);
		classProperty.setCaretPosition(0);

		this.add(new JScrollPane(classProperty));
	}

	private void printUnit(JDependUnit unit) {

		OutputStream info = new ByteArrayOutputStream();

		TextSummaryPrinter printer = new TextSummaryPrinter();

		printer.setStream(info);

		printer.printPackage(unit);

		printer.getWriter().flush();

		detailText = info.toString();

		try {
			info.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
