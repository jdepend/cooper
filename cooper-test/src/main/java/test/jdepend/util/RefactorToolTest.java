package test.jdepend.util;

import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.metadata.util.ClassSearchUtil;
import jdepend.model.Component;
import jdepend.model.component.JarComponent;
import jdepend.model.result.AnalysisResult;
import jdepend.service.local.JDependLocalService;
import jdepend.service.local.ServiceFactory;
import jdepend.util.refactor.RefactorTool;
import jdepend.util.refactor.RefactorToolFactory;
import junit.framework.TestCase;

public class RefactorToolTest extends TestCase {

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

	public void testIdentify() throws JDependException {

		RefactorTool tool = RefactorToolFactory.createTool();
		
		List<Component> components = result.getComponents();
//		if(components.)
//		tool.moveClass(javaClasses, target)

	}

}
