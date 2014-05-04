package jdepend.report.history;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.apache.commons.jrcs.diff.Diff;
import org.apache.commons.jrcs.diff.DifferentiationFailedException;
import org.apache.commons.jrcs.diff.Revision;
import org.jmeld.JMeldException;
import org.jmeld.ui.BufferDiffPanel;
import org.jmeld.ui.JMeldPanel;
import org.jmeld.util.node.JMDiffNode;
import org.jmeld.util.node.JMDiffNodeFactory;

public class ReportHistoryComparor {

	public static String compare(String group, String v1, String v2) {

		String file1 = (new ReportHistory(group)).getFilePath(v1);
		String file2 = (new ReportHistory(group)).getFilePath(v2);

		try {
			Object[] orig = loadFile(file1);
			Object[] rev = loadFile(file2);

			Diff df = new Diff(orig);
			Revision r = df.diff(rev);

			return r.toString();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (DifferentiationFailedException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static JComponent comparePanel(String group, String v1, String v2) {

		File leftFile = new File((new ReportHistory(group)).getFilePath(v1));
		File rightFile = new File((new ReportHistory(group)).getFilePath(v2));

		JMDiffNode diffNode = JMDiffNodeFactory.create(leftFile.getName(), leftFile, rightFile.getName(), rightFile);
		try {
			diffNode.diff();
		} catch (JMeldException e) {
			e.printStackTrace();
		}

		BufferDiffPanel panel = new BufferDiffPanel(new JMeldPanel());
		panel.setDiffNode(diffNode);
		panel.doGoToFirst();

		return panel;

	}

	static final String[] loadFile(String name) throws IOException {
		BufferedReader data = new BufferedReader(new FileReader(name));
		List lines = new ArrayList();
		String s;

		while ((s = data.readLine()) != null) {
			lines.add(s);
		}

		return (String[]) lines.toArray(new String[lines.size()]);
	}

}
