package test.jdepend.util;

import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.metadata.util.ClassSearchUtil;
import jdepend.model.Component;
import jdepend.model.component.JarComponent;
import jdepend.model.result.AnalysisResult;
import jdepend.service.local.JDependLocalService;
import jdepend.service.local.ServiceFactory;
import jdepend.util.todolist.TODOItem;
import jdepend.util.todolist.TODOListIdentify;
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

	public void testIdentify() throws JDependException {

		TODOListIdentify identifyer = new TODOListIdentify();

		List<TODOItem> items = identifyer.identify(result);

		for (TODOItem item : items) {
			System.out.println(item.getInfo());
		}
	}

}
