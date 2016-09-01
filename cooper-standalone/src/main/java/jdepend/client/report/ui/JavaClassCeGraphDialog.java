package jdepend.client.report.ui;

import jdepend.client.report.util.TreeGraphUtil;
import jdepend.framework.ui.dialog.CooperDialog;
import jdepend.metadata.tree.JavaClassTree;
import jdepend.metadata.tree.TreeCreatorFacade;
import jdepend.model.JavaClassUnit;

public class JavaClassCeGraphDialog extends CooperDialog {

	public JavaClassCeGraphDialog(JavaClassUnit javaClass) {
		super(javaClass.getName() + "传出图形");

		JavaClassTree tree = TreeCreatorFacade.createJavaClassCeTree(javaClass.getJavaClass());

		if (tree != null) {
			this.add((new TreeGraphUtil()).createTree(tree.getRoots().get(0)));
		}
	}

}
