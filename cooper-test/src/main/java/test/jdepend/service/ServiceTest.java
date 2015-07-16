package test.jdepend.service;

import test.common.TestConfigUtil;
import jdepend.framework.util.MetricsFormat;
import jdepend.model.Component;
import jdepend.model.Relation;
import jdepend.model.component.JarComponent;
import jdepend.model.result.AnalysisResult;
import jdepend.service.JDependLocalService;
import jdepend.service.ServiceFactory;
import junit.framework.TestCase;

public class ServiceTest extends TestCase {

	private AnalysisResult result;

	@Override
	protected void setUp() throws Exception {

		JDependLocalService service = ServiceFactory.createJDependLocalService();

		for (String p : TestConfigUtil.getSelfPath()) {
			service.addDirectory(p);
		}

		Component component = new JarComponent();
		service.setComponent(component);

		result = service.analyze();
	}

	public void testResult() {
		System.out.println(result);
	}

	public void testScore() {
		System.out.println("\nScore(总分):" + MetricsFormat.toFormattedMetrics(result.getScore()));
		System.out.println("D(抽象程度合理性得分):" + MetricsFormat.toFormattedMetrics(result.getDistance()));
		System.out.println("Balance(內聚性得分):" + MetricsFormat.toFormattedMetrics(result.getBalance()));
		System.out.println("Encapsulation(封装性得分):" + MetricsFormat.toFormattedMetrics(result.getEncapsulation()));
		System.out.println("RelationRationality(关系合理性性得分):"
				+ MetricsFormat.toFormattedMetrics(result.getRelationRationality()));
	}

	public void testComponentList() {
		for (Component component : result.getComponents()) {
			System.out.println("\nName:" + component.getName());
			System.out.println("Area:" + component.getAreaComponent());
			System.out.println("Layer(所属层次):" + component.getLayerDesc());
			System.out.println("Stability(稳定性):" + MetricsFormat.toFormattedMetrics(component.getStability()));
			System.out.println("Abstractness(抽象性):" + MetricsFormat.toFormattedMetrics(component.getAbstractness()));
			System.out.println("Encapsulation(封装性):" + MetricsFormat.toFormattedMetrics(component.getEncapsulation()));
			System.out.println("Balance(内聚性):" + MetricsFormat.toFormattedMetrics(component.getBalance()));
			System.out.println("Distance(抽象程度合理性):" + MetricsFormat.toFormattedMetrics(component.getDistance()));
			System.out.println("Cohesion(内聚值):" + MetricsFormat.toFormattedMetrics(component.getCohesion()));
			System.out.println("Coupling(耦合值):" + MetricsFormat.toFormattedMetrics(component.getCoupling()));
			System.out.println("Ca(传入):" + component.getAfferentCoupling());
			System.out.println("Ce(传出):" + component.getEfferentCoupling());
			System.out.println("CN(类数量):" + component.getClassCount());
			System.out.println("AC(抽象类数量):" + component.getAbstractClassCount());
			System.out.println("LC(代码行数):" + component.getLineCount());
		}
	}

	public void testRelationList() {
		for (Relation relation : result.getRelations()) {
			System.out.println("\nCurrent:" + relation.getCurrent().getName());
			System.out.println("Depend:" + relation.getDepend().getName());
			System.out.println("Intensity(关系强度):" + MetricsFormat.toFormattedMetrics(relation.getIntensity()));
			System.out.println("Balance(关系关联的组件内聚值之和与关系强度之差):"
					+ MetricsFormat.toFormattedMetrics(relation.getBalance()));
			System.out.println("isNormality(是否为正常关系):" + relation.isNormality());
			System.out.println("isAttention(是否值得关注):" + relation.isAttention());
			System.out.println("AttentionType(关注类型):" + relation.getAttentionTypeName());
		}
	}

}
