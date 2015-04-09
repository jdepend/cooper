package jdepend.core.local.serviceproxy;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.util.List;

import jdepend.core.framework.serviceproxy.AbstractJDependServiceProxy;
import jdepend.framework.exception.JDependException;
import jdepend.framework.file.AnalyzeData;
import jdepend.model.Component;
import jdepend.model.result.AnalysisResult;
import jdepend.parse.BuildListener;
import jdepend.parse.ParseListener;
import jdepend.service.local.AnalyseListener;
import jdepend.service.local.JDependLocalService;

public class JDependServiceLocalProxy extends AbstractJDependServiceProxy {

	private JDependLocalService service;

	private final static String JDependLocalServiceImplClassName = "jdepend.service.local.impl.JDependLocalServiceImpl";

	public JDependServiceLocalProxy(String groupName, String commandName) {
		try {
			Constructor<JDependLocalService> constructor = (Constructor<JDependLocalService>) Class.forName(
					JDependLocalServiceImplClassName).getConstructor(String.class, String.class);
			this.service = constructor.newInstance(groupName, commandName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addParseListener(ParseListener listener) {
		service.addParseListener(listener);
	}

	public void addBuildListener(BuildListener listener) {
		service.addBuildListener(listener);

	}

	public void addAnalyseListener(AnalyseListener listener) {
		service.addAnalyseListener(listener);
	}

	public void addDirectory(String name) throws JDependException {
		try {
			service.addDirectory(name);
		} catch (IOException e) {
			e.printStackTrace();
			throw new JDependException(e);
		}

	}

	protected AnalysisResult doAnalyze() throws JDependException {
		return service.analyze();
	}

	public int countClasses() {
		return service.countClasses();
	}

	public void setComponent(Component component) {
		service.setComponent(component);
	}

	public void setLogWriter(PrintWriter printWriter) {
		service.setParseLogWriter(printWriter);

	}

	public void addFilteredPackages(List<String> filteredPackages) {
		service.addFilteredPackages(filteredPackages);

	}

	@Override
	public void setAnalyseData(AnalyzeData data) {
		this.service.setAnalyzeData(data);
	}
}
