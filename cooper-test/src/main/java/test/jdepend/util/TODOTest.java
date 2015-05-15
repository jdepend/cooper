package test.jdepend.util;

import java.util.List;

import jdepend.core.framework.serviceproxy.JDependServiceProxy;
import jdepend.core.local.serviceproxy.JDependServiceLocalProxy;
import jdepend.framework.exception.JDependException;
import jdepend.framework.util.MetricsFormat;
import jdepend.metadata.util.ClassSearchUtil;
import jdepend.model.Component;
import jdepend.model.component.JarComponent;
import jdepend.model.result.AnalysisResult;
import jdepend.util.refactor.AdjustHistory;
import jdepend.util.refactor.Memento;
import jdepend.util.todolist.TODOItem;
import jdepend.util.todolist.TODOListIdentify;
import junit.framework.TestCase;

public class TODOTest extends TestCase {

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

	public void testIdentify() throws JDependException {

		TODOListIdentify identifyer = new TODOListIdentify();

		List<TODOItem> items = identifyer.identify(result);

		for (TODOItem item : items) {
			System.out.println(item.getContent());
			System.out.println(item.getAccording());
			System.out.println();
		}
	}

	public void testExecute() throws JDependException {

		TODOListIdentify identifyer = new TODOListIdentify();

		List<TODOItem> items = identifyer.identify(result);

		if (items.size() > 0) {

			System.out.println(items.get(0).getInfo());

			items.get(0).execute();

			List<Memento> mementos = AdjustHistory.getInstance().getMementos();

			if (mementos.size() > 0) {
				System.out
						.println("调整前分数 :" + MetricsFormat.toFormattedMetrics(mementos.get(0).getResult().getScore()));
				System.out.println("调整后分数 :"
						+ MetricsFormat.toFormattedMetrics(AdjustHistory.getInstance().getCurrent().getScore()));
			}

		}
	}
}
