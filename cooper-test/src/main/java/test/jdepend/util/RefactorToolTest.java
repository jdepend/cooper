package test.jdepend.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jdepend.core.serviceproxy.JDependServiceLocalProxy;
import jdepend.core.serviceproxy.framework.JDependServiceProxy;
import jdepend.framework.exception.JDependException;
import jdepend.framework.util.MetricsFormat;
import jdepend.metadata.util.ClassSearchUtil;
import jdepend.model.Component;
import jdepend.model.Relation;
import jdepend.model.component.JarComponent;
import jdepend.model.result.AnalysisResult;
import jdepend.util.refactor.AdjustHistory;
import jdepend.util.refactor.Memento;
import jdepend.util.refactor.RefactorTool;
import jdepend.util.refactor.RefactorToolFactory;
import junit.framework.TestCase;

public class RefactorToolTest extends TestCase {

	private AnalysisResult result;

	@Override
	protected void setUp() throws Exception {

		JDependServiceProxy serviceProxy = new JDependServiceLocalProxy("测试组", "测试命令");

		for (String p : ClassSearchUtil.getSelfPath()) {
			serviceProxy.addDirectory(p);
		}

		Component component = new JarComponent();
		serviceProxy.setComponent(component);

		result = serviceProxy.analyze();
	}

	public void testUniteComponent() throws JDependException {

		RefactorTool tool = RefactorToolFactory.createTool();

		Relation maxIntensityRelation = null;
		for (Relation relation : result.getRelations()) {
			if (maxIntensityRelation == null || maxIntensityRelation.getIntensity() < relation.getIntensity()) {
				maxIntensityRelation = relation;
			}
		}

		if (maxIntensityRelation != null) {

			Collection<String> components = new ArrayList<String>();
			components.add(maxIntensityRelation.getCurrent().getComponent().getName());
			components.add(maxIntensityRelation.getDepend().getComponent().getName());

			String name = maxIntensityRelation.getCurrent().getComponent().getName() + "|"
					+ maxIntensityRelation.getDepend().getComponent().getName();

			tool.uniteComponent(name, 0, components);

			List<Memento> mementos = AdjustHistory.getInstance().getMementos();

			if (mementos.size() > 0) {
				float score1 = MetricsFormat.toFormattedMetrics(mementos.get(0).getResult().getScore());
				float score2 = MetricsFormat.toFormattedMetrics(AdjustHistory.getInstance().getCurrent().getScore());
				System.out.println("调整前分数 :" + score1);
				System.out.println("调整后分数 :" + score2);
			}
		}
	}
}
