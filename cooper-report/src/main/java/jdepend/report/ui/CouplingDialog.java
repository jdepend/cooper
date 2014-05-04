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

public class CouplingDialog extends CooperDialog {

	private StringBuilder couplingText;

	public CouplingDialog(String name) {

		super(name + " 耦合值（倒序）");

		getContentPane().setLayout(new BorderLayout());

		JDependUnit unit = JDependUnitMgr.getInstance().getUnit(name);

		printCoupling(unit);

		this.add(new JScrollPane((new XMLJDependUtil()).createResult(couplingText)));
	}

	private void printCoupling(JDependUnit unit) {

		OutputStream info = new ByteArrayOutputStream();

		JDependPrinter printer = new JDependPrinter();

		printer.setStream(info);

		printer.printCoupling(unit);

		printer.getWriter().flush();

		couplingText = new StringBuilder(info.toString());

		try {
			info.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
