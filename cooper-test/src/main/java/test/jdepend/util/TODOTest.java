package test.jdepend.util;

import jdepend.framework.util.MetricsFormat;
import jdepend.metadata.util.ClassSearchUtil;
import jdepend.model.Component;
import jdepend.model.Relation;
import jdepend.model.component.JarComponent;
import jdepend.model.result.AnalysisResult;
import jdepend.service.local.JDependLocalService;
import jdepend.service.local.ServiceFactory;
import junit.framework.TestCase;

public class TODOTest extends TestCase {

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

	

}
