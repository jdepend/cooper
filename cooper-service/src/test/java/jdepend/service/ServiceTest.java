package jdepend.service;

import jdepend.model.Component;
import jdepend.model.component.JarComponent;
import jdepend.model.result.AnalysisResult;
import jdepend.service.local.JDependLocalService;
import junit.framework.TestCase;

public class ServiceTest extends TestCase {

	private AnalysisResult result;

	@Override
	protected void setUp() throws Exception {

		JDependLocalService service = ServiceFactory.createJDependLocalService();

		service.addDirectory("E:\\my_workspace\\project\\Cooper\\projects\\cooper\\targetjar\\spring-data-mongodb-1.1.0.M1.jar");
		service.addDirectory("E:\\my_workspace\\project\\Cooper\\projects\\cooper\\targetjar\\spring-data-commons-core-1.3.0.RELEASE.jar");
		service.addDirectory("E:\\my_workspace\\project\\Cooper\\projects\\cooper\\targetjar\\spring-data-jpa-1.1.0.RELEASE.jar");
		service.addDirectory("E:\\my_workspace\\project\\Cooper\\projects\\cooper\\targetjar\\spring-data-mongodb-cross-store-1.1.0.M1.jar");
		service.addDirectory("E:\\my_workspace\\project\\Cooper\\projects\\cooper\\targetjar\\spring-data-mongodb-log4j-1.1.0.M1.jar");

		Component component = new JarComponent();
		service.setComponent(component);

		result = service.analyze();
	}

	public void testPrintResult() {
		System.out.println(result);
	}

}
