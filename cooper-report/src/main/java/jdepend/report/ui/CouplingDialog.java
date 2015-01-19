package jdepend.report.ui;

import java.awt.BorderLayout;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JScrollPane;

import jdepend.framework.ui.CooperDialog;
import jdepend.model.JDependUnit;
import jdepend.model.JDependUnitMgr;
import jdepend.report.way.textui.JDependPrinter;

public class CouplingDialog extends CooperDialog {

	private StringBuilder couplingText;

	public CouplingDialog(JDependUnit unit) {

		super(unit.getName() + " 耦合值（倒序）");

		getContentPane().setLayout(new BorderLayout());

		Collection<JDependUnit> units = new ArrayList<JDependUnit>();
		units.add(unit);

		printCoupling(units);

		this.add(new JScrollPane((new XMLJDependUtil()).createResult(couplingText)));
	}

	public CouplingDialog() {

		super("耦合值（倒序）");

		getContentPane().setLayout(new BorderLayout());

		printCoupling(JDependUnitMgr.getInstance().getComponents());

		this.add(new JScrollPane((new XMLJDependUtil()).createResult(couplingText)));
	}

	private void printCoupling(Collection<? extends JDependUnit> units) {

		OutputStream info = new ByteArrayOutputStream();

		JDependPrinter printer = new JDependPrinter();

		printer.setStream(info);

		printer.printCouplings(units);

		printer.getWriter().flush();

		couplingText = new StringBuilder(info.toString());

		try {
			info.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
