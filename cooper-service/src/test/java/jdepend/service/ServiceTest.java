package jdepend.service;

import jdepend.framework.util.MetricsFormat;
import jdepend.metadata.util.ClassSearchUtil;
import jdepend.model.Component;
import jdepend.model.component.JarComponent;
import jdepend.model.result.AnalysisResult;
import jdepend.service.local.JDependLocalService;
import jdepend.service.local.ServiceFactory;
import junit.framework.TestCase;

public class ServiceTest extends TestCase {

	private AnalysisResult result;

	@Override
	protected void setUp() throws Exception {

		JDependLocalService service = ServiceFactory.createJDependLocalService();

		for (String p : ClassSearchUtil.getSelfPath()) {
			service.addDirectory(p);
		}

		Component component = new JarComponent();
		service.setComponent(component);

		result = service.analyze();
	}

	public void testPrintResult() {
		System.out.println(result);
	}

	public void testComponentList() {
		for (Component component : result.getComponents()) {
			System.out.println("\nName:" + component.getName());
			System.out.println("Area:" + component.getAreaComponent());
			System.out.println("Stability(稳定性):" + MetricsFormat.toFormattedMetrics(component.getStability()));
			System.out.println("Abstractness(抽象性):" + MetricsFormat.toFormattedMetrics(component.getAbstractness()));
			System.out.println("Encapsulation(封装性):" + MetricsFormat.toFormattedMetrics(component.getEncapsulation()));
			System.out.println("Balance(内聚性):" + MetricsFormat.toFormattedMetrics(component.getBalance()));
			System.out.println("Distance(抽象程度合理性):" + MetricsFormat.toFormattedMetrics(component.getDistance()));
			System.out.println("Cohesion(内聚值):" + MetricsFormat.toFormattedMetrics(component.getCohesion()));
			System.out.println("Coupling(耦合值):" + MetricsFormat.toFormattedMetrics(component.getCoupling()));
		}
	}

}
