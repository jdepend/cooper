package test.jdepend.util;

import java.util.List;

import test.common.TestConfigUtil;

import jdepend.core.serviceproxy.JDependServiceLocalProxy;
import jdepend.core.serviceproxy.framework.JDependServiceProxy;
import jdepend.framework.exception.JDependException;
import jdepend.framework.util.MetricsFormat;
import jdepend.model.Component;
import jdepend.model.component.JarComponent;
import jdepend.model.result.AnalysisResult;
import jdepend.util.refactor.AdjustHistory;
import jdepend.util.refactor.Memento;
import jdepend.util.todolist.TODOItem;
import jdepend.util.todolist.TODOListIdentifyerFacade;
import junit.framework.TestCase;

public class TODOTest extends TestCase {

	private AnalysisResult result;

	@Override
	protected void setUp() throws Exception {

		JDependServiceProxy serviceProxy = new JDependServiceLocalProxy("测试组", "测试命令");

		for (String p : TestConfigUtil.getSelfPath()) {
			serviceProxy.addDirectory(p);
		}

		Component component = new JarComponent();
		serviceProxy.setComponent(component);

		result = serviceProxy.analyze();
	}

	public void testIdentify() throws JDependException {

		TODOListIdentifyerFacade identifyer = new TODOListIdentifyerFacade();

		List<TODOItem> items = identifyer.identify(result);

		for (TODOItem item : items) {
			System.out.println(item.getContent());
			System.out.println(item.getAccording());
			System.out.println();
		}
	}

	public void testExecute() throws JDependException {

		TODOListIdentifyerFacade identifyer = new TODOListIdentifyerFacade();

		List<TODOItem> items = identifyer.identify(result);

		if (items.size() > 0) {

			TODOItem item = items.get(0);

			item.execute();
			System.out.println(item.getInfo());

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
