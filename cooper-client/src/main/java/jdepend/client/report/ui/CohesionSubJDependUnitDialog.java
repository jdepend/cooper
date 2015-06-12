package jdepend.client.report.ui;

import java.awt.BorderLayout;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JScrollPane;

import jdepend.framework.ui.dialog.CooperDialog;
import jdepend.model.SubJDependUnit;
import jdepend.client.report.way.textui.JDependPrinter;

public class CohesionSubJDependUnitDialog extends CooperDialog {

	private StringBuilder cohesionText;

	public CohesionSubJDependUnitDialog(SubJDependUnit unit) {

		super(unit.getName() + " 内聚值");

		getContentPane().setLayout(new BorderLayout());

		printCohesion(unit);

		this.add(new JScrollPane((new XMLJDependUtil()).createResult(cohesionText)));
	}

	private void printCohesion(SubJDependUnit unit) {

		OutputStream info = new ByteArrayOutputStream();

		JDependPrinter printer = new JDependPrinter();

		printer.setStream(info);

		printer.printSubJDependUnitCohesion(unit);

		printer.getWriter().flush();

		cohesionText = new StringBuilder(info.toString());

		try {
			info.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
