package jdepend.client.report.ui;

import jdepend.client.report.util.TreeGraphUtil;
import jdepend.framework.ui.dialog.CooperDialog;
import jdepend.metadata.tree.JavaClassCeTreeCreator;
import jdepend.metadata.tree.JavaClassTree;
import jdepend.model.JavaClassUnit;

public class JavaClassCeGraphDialog extends CooperDialog {

	public JavaClassCeGraphDialog(JavaClassUnit javaClass) {
		super(javaClass.getName() + "传出图形");

		JavaClassTree tree = (new JavaClassCeTreeCreator()).create(javaClass.getJavaClass());

		if (tree != null) {
			this.add((new TreeGraphUtil()).createTree(tree.getRoots().get(0)));
		}
	}

}
