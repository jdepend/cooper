package jdepend.client.report.ui;

import jdepend.framework.ui.dialog.CooperDialog;
import jdepend.metadata.tree.JavaClassCaTreeCreator;
import jdepend.metadata.tree.JavaClassTree;
import jdepend.model.JDependUnitMgr;
import jdepend.model.JavaClassUnit;
import jdepend.model.result.AnalysisResult;
import jdepend.model.util.JavaClassUnitUtil;
import jdepend.client.report.util.TreeGraphUtil;

public class JavaClassCaGraphDialog extends CooperDialog {

	public JavaClassCaGraphDialog(JavaClassUnit javaClass) {
		super(javaClass.getName() + "传入图形");

		AnalysisResult result = JDependUnitMgr.getInstance().getResult();
		JavaClassTree tree = (new JavaClassCaTreeCreator()).create(javaClass.getJavaClass(),
				JavaClassUnitUtil.getJavaClasses(result.getClasses()));

		if (tree != null) {
			this.add((new TreeGraphUtil()).createTree(tree.getRoots().get(0)));
		}
	}

}
