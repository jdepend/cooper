package jdepend.client.core.serviceproxy;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

import jdepend.core.serviceproxy.framework.AbstractJDependServiceProxy;
import jdepend.framework.exception.JDependException;
import jdepend.framework.file.AnalyzeData;
import jdepend.metadata.JavaPackage;
import jdepend.model.Component;
import jdepend.model.result.AnalysisResult;
import jdepend.parse.BuildListener;
import jdepend.parse.ParseListener;
import jdepend.service.AnalyseListener;
import jdepend.service.JDependLocalService;
import jdepend.service.impl.JDependLocalServiceImpl;

public class JDependServiceLocalProxy extends AbstractJDependServiceProxy {

	private JDependLocalService service;

	public JDependServiceLocalProxy(String groupName, String commandName) {
		this.service = new JDependLocalServiceImpl(groupName, commandName);
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

	public void addCommandFilteredPackages(List<String> filteredPackages) {
		service.addCommandFilteredPackages(filteredPackages);

	}

	@Override
	public void setAnalyseData(AnalyzeData data) {
		this.service.setAnalyzeData(data);
	}

	@Override
	public Collection<JavaPackage> getPackages() throws JDependException {
		return this.service.getPackages();
	}
}
