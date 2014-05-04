package jdepend.report.ui;

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

public class CycleDialog extends CooperDialog {

	private String cycleText;

	public CycleDialog(String name) {

		super(name);

		getContentPane().setLayout(new BorderLayout());

		TextViewer classProperty = new TextViewer();

		JDependUnit unit = JDependUnitMgr.getInstance().getUnit(name);
		printCycle(unit);

		classProperty.setText(cycleText);
		classProperty.setCaretPosition(0);

		this.add(new JScrollPane(classProperty));
	}

	private void printCycle(JDependUnit unit) {

		OutputStream info = new ByteArrayOutputStream();

		TextSummaryPrinter printer = new TextSummaryPrinter();

		printer.setStream(info);

		printer.printCycle(unit);

		printer.getWriter().flush();

		cycleText = info.toString();

		try {
			info.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
