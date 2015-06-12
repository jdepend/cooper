package jdepend.client.report.way.swingui;

import java.awt.Font;
import java.awt.GridLayout;
import java.util.Collection;

import javax.swing.JPanel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import jdepend.model.Component;
import jdepend.model.component.MemoryComponent;

/**
 * The <code>JDepend</code> class analyzes directories of Java class files,
 * generates metrics for each Java package, and reports the metrics in a Swing
 * tree.
 * 
 * @author <b>Abner</b>
 * 
 */

public class SwingJDepend {

	private DependTree afferentTree;

	private DependTree efferentTree;

	private static Font BOLD_FONT = new Font("dialog", Font.BOLD, 12);

	/**
	 * Constructs a <code>JDepend</code> instance.
	 */
	public SwingJDepend() {
	}

	protected void updateTree(Collection<Component> packages) {

		MemoryComponent jPackage = new MemoryComponent("root");
		jPackage.setAfferents(packages);
		jPackage.setEfferents(packages);

		AfferentNode ah = new AfferentNode(null, jPackage);
		getAfferentTree().setModel(new DependTreeModel(ah));

		EfferentNode eh = new EfferentNode(null, jPackage);
		getEfferentTree().setModel(new DependTreeModel(eh));
	}

	private JPanel createTreePanel() {

		JPanel panel = new JPanel();

		panel.setLayout(new GridLayout(2, 1));
		panel.add(getEfferentTree());
		panel.add(getAfferentTree());

		return panel;
	}

	private DependTree getAfferentTree() {
		if (afferentTree == null) {
			afferentTree = new DependTree();
			afferentTree.addTreeSelectionListener(new TreeListener());
		}

		return afferentTree;
	}

	private DependTree getEfferentTree() {
		if (efferentTree == null) {
			efferentTree = new DependTree();
			efferentTree.addTreeSelectionListener(new TreeListener());
		}

		return efferentTree;
	}

	//
	// Tree selection handler.
	//
	private class TreeListener implements TreeSelectionListener {

		/**
		 * Constructs a <code>TreeListener</code> instance.
		 */
		TreeListener() {
		}

		/**
		 * Callback method triggered whenever the value of the tree selection
		 * changes.
		 * 
		 * @param te
		 *            Event that characterizes the change.
		 */
		public void valueChanged(TreeSelectionEvent te) {

			TreePath path = te.getNewLeadSelectionPath();

			if (path != null) {
				PackageNode node = (PackageNode) path.getLastPathComponent();
			}
		}
	}

	public JPanel getResult(Collection<Component> units) {

		JPanel result = createTreePanel();

		updateTree(units);

		return result;

	}
}
