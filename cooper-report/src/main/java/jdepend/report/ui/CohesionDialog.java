package jdepend.report.ui;

import java.awt.BorderLayout;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JScrollPane;

import jdepend.framework.ui.CooperDialog;
import jdepend.model.JDependUnit;
import jdepend.model.JDependUnitMgr;
import jdepend.report.way.textui.JDependPrinter;

public class CohesionDialog extends CooperDialog {

	private StringBuilder cohesionText;

	public CohesionDialog(String name) {

		super(name + " 内聚值（正序）");

		getContentPane().setLayout(new BorderLayout());

		JDependUnit unit = JDependUnitMgr.getInstance().getUnit(name);

		printCohesion(unit);

		this.add(new JScrollPane((new XMLJDependUtil()).createResult(cohesionText)));
	}

	private void printCohesion(JDependUnit unit) {

		OutputStream info = new ByteArrayOutputStream();

		JDependPrinter printer = new JDependPrinter();

		printer.setStream(info);

		printer.printCohesion(unit);

		printer.getWriter().flush();

		cohesionText = new StringBuilder(info.toString());

		try {
			info.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
