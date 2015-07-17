package jdepend.client.report.ui;

import jdepend.client.report.util.TreeGraphUtil;
import jdepend.framework.ui.dialog.CooperDialog;
import jdepend.metadata.tree.JavaClassTree;
import jdepend.metadata.tree.TreeCreatorFacade;
import jdepend.model.JavaClassUnit;

public class JavaClassCaGraphDialog extends CooperDialog {

	public JavaClassCaGraphDialog(JavaClassUnit javaClass) {
		super(javaClass.getName() + "传入图形");

		JavaClassTree tree = TreeCreatorFacade.createJavaClassCaTree(javaClass.getJavaClass());

		if (tree != null) {
			this.add((new TreeGraphUtil()).createTree(tree.getRoots().get(0)));
		}
	}

}
