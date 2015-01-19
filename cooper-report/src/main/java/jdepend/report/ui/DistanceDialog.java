package jdepend.report.ui;

import java.awt.BorderLayout;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JScrollPane;

import jdepend.framework.ui.CooperDialog;
import jdepend.model.JDependUnit;
import jdepend.report.way.textui.JDependPrinter;

public class DistanceDialog extends CooperDialog {

	private StringBuilder distanceText;

	public DistanceDialog(JDependUnit unit) {

		super(unit.getName() + " 不稳定性");

		getContentPane().setLayout(new BorderLayout());

		printCoupling(unit);

		this.add(new JScrollPane((new XMLJDependUtil()).createResult(distanceText)));
	}

	private void printCoupling(JDependUnit unit) {

		OutputStream info = new ByteArrayOutputStream();

		JDependPrinter printer = new JDependPrinter();

		printer.setStream(info);

		printer.printDistance(unit);

		printer.getWriter().flush();

		distanceText = new StringBuilder(info.toString());

		try {
			info.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
