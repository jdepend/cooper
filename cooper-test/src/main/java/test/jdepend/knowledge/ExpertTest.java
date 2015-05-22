package test.jdepend.knowledge;

import jdepend.core.framework.serviceproxy.JDependServiceProxy;
import jdepend.core.local.serviceproxy.JDependServiceLocalProxy;
import jdepend.framework.exception.JDependException;
import jdepend.knowledge.domainanalysis.AdviseInfo;
import jdepend.knowledge.domainanalysis.Expert;
import jdepend.knowledge.domainanalysis.ExpertFactory;
import jdepend.knowledge.domainanalysis.Structure;
import jdepend.knowledge.domainanalysis.StructureCategory;
import jdepend.metadata.util.ClassSearchUtil;
import jdepend.model.Component;
import jdepend.model.component.JarComponent;
import jdepend.model.result.AnalysisResult;
import junit.framework.TestCase;

public class ExpertTest extends TestCase {

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

	public void testArchitectPatternDomainAnalysis() throws JDependException {

		Expert expert = new ExpertFactory().createExpert();

		Structure structure = new Structure();
		structure.setCategory(StructureCategory.ArchitectPatternDomainAnalysis);
		structure.setData(result);

		AdviseInfo advise = expert.advise(structure);
		if (advise != null) {
			System.out.print(advise.toString());
		}
	}
}
