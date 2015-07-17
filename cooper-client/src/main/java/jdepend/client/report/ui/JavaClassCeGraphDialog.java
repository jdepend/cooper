package jdepend.client.report.ui;

import jdepend.client.report.util.TreeGraphUtil;
import jdepend.framework.ui.dialog.CooperDialog;
import jdepend.metadata.tree.JavaClassTree;
import jdepend.metadata.tree.TreeCreatorFactory;
import jdepend.model.JavaClassUnit;

public class JavaClassCeGraphDialog extends CooperDialog {

	public JavaClassCeGraphDialog(JavaClassUnit javaClass) {
		super(javaClass.getName() + "传出图形");

		JavaClassTree tree = TreeCreatorFactory.createJavaClassCeTreeCreator().create(javaClass.getJavaClass());

		if (tree != null) {
			this.add((new TreeGraphUtil()).createTree(tree.getRoots().get(0)));
		}
	}

}
