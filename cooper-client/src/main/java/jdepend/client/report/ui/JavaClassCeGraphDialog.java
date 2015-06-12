package jdepend.client.report.ui;

import jdepend.framework.ui.dialog.CooperDialog;
import jdepend.metadata.tree.JavaClassCeTreeCreator;
import jdepend.metadata.tree.JavaClassTree;
import jdepend.model.JDependUnitMgr;
import jdepend.model.JavaClassUnit;
import jdepend.model.result.AnalysisResult;
import jdepend.model.util.JavaClassUnitUtil;
import jdepend.client.report.util.TreeGraphUtil;

public class JavaClassCeGraphDialog extends CooperDialog {

	public JavaClassCeGraphDialog(JavaClassUnit javaClass) {
		super(javaClass.getName() + "传出图形");

		AnalysisResult result = JDependUnitMgr.getInstance().getResult();
		JavaClassTree tree = (new JavaClassCeTreeCreator()).create(javaClass.getJavaClass(),
				JavaClassUnitUtil.getJavaClasses(result.getClasses()));

		if (tree != null) {
			this.add((new TreeGraphUtil()).createTree(tree.getRoots().get(0)));
		}
	}

}
