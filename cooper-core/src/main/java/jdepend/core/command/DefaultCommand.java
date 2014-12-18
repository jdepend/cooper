package jdepend.core.command;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jdepend.core.serviceproxy.JDependServiceProxy;
import jdepend.core.serviceproxy.JDependServiceProxyFactory;
import jdepend.framework.exception.JDependException;
import jdepend.model.Component;
import jdepend.model.Metrics;
import jdepend.model.result.AnalysisResult;
import jdepend.parse.ParseListener;
import jdepend.service.local.AnalyseListener;

/**
 * The <code>JDepend</code> class analyzes directories of Java class files,
 * generates metrics for each Java package, and reports the metrics in a textual
 * format.
 * 
 * @author <b>Abner</b>
 */

public class DefaultCommand implements Command {

	protected JDependServiceProxy serviceProxy;

	private int javaClassCount = -1;

	private String group;

	private String name;
	// 扩展指标
	private Map<String, Metrics> extendMetrics = new LinkedHashMap<String, Metrics>();

	public DefaultCommand() {
	}

	public DefaultCommand(String group, String name) {
		this.group = group;
		this.name = name;
		this.serviceProxy = new JDependServiceProxyFactory().getJDependServiceProxy(group, name);
	}

	/**
	 * 分析前的准备
	 * 
	 * @throws JDependException
	 */
	public void prepareAnalyze() throws JDependException {
		// 设置外部指标
		for (String name : extendMetrics.keySet()) {
			this.serviceProxy.registMetrics(name, extendMetrics.get(name));
		}
	}

	public void addFilteredPackages(List<String> filteredPackages) {
		this.serviceProxy.addFilteredPackages(filteredPackages);
	}

	/**
	 * 设置日志输出的对象
	 * 
	 * @param writer
	 */
	public void setLogStream(OutputStream stream) {
		this.serviceProxy.setLogWriter(new PrintWriter(stream));
	}

	/**
	 * Adds the specified directory name to the collection of directories to be
	 * analyzed.
	 * 
	 * @param name
	 *            Directory name.
	 * @throws JDependException
	 */
	public void addDirectory(String name) throws JDependException {
		serviceProxy.addDirectory(name);
	}

	/**
	 * Analyzes the registered directories, generates metrics for each Java
	 * package, and reports the metrics.
	 * 
	 * @throws JDependException
	 */
	private AnalysisResult analyze() throws JDependException {
		return serviceProxy.analyze();
	}

	protected void usage(String message) {
		if (message != null) {
			System.err.println("\n" + message);
		}
		String baseUsage = "\nJDepend ";

		System.err.println("");
		System.err.println("usage: ");
		System.err.println(baseUsage + "[-components <components>]" + " [-file <output file>] <directory> "
				+ "[directory2 [directory 3] ...]");
		// System.exit(1);
	}

	/**
	 * 1、可以通过-componentClassName设置新的组件类名
	 * 
	 * 2、可以通过-components设置以输入的字符串开头的一组package为一个组件，组件间以;分开
	 * 
	 * 3、可以通过-metrics设置定制的指标
	 */
	public void initArgs(String[] args) throws JDependException {
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				if (args[i].equalsIgnoreCase("-componentClassName")) {
					if (args.length <= i + 1) {
						usage("componentClassName not specified.");
					}
					try {
						Component component = (Component) DefaultCommand.class.getClassLoader().loadClass(args[++i])
								.newInstance();
						if (args.length > i + 1 && !args[i + 1].startsWith("-"))
							component.init(this.group, this.name, args[++i]);
						else
							component.init(this.group, this.name, null);
						serviceProxy.setComponent(component);

					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (args[i].equalsIgnoreCase("-components")) {
					if (args.length <= i + 1) {
						usage("Components not specified.");
					}
					serviceProxy.setComponent(SimpleComponent.calSimpleComponent(args[++i], ";"));
				} else if (args[i].equalsIgnoreCase("-metrics")) {
					if (args.length <= i + 1) {
						usage("metrics not specified.");
					}
					try {
						Metrics metrics = (Metrics) DefaultCommand.class.getClassLoader().loadClass(args[++i])
								.newInstance();
						serviceProxy.registMetrics(args[i], metrics);
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {
					i++;
				}
			}
		}
	}

	@Override
	public AnalysisResult execute() throws JDependException {
		this.prepareAnalyze();
		return analyze();
	}

	@Override
	public int getTaskSize() {
		return this.getJavaClassCount();
	}

	private int getJavaClassCount() {
		if (this.javaClassCount == -1) {
			this.javaClassCount = this.serviceProxy.countClasses();
		}
		return this.javaClassCount;
	}

	@Override
	public void addParseListener(ParseListener listener) throws JDependException {
		this.serviceProxy.addParseListener(listener);
	}

	@Override
	public void addAnalyseListener(AnalyseListener listener) throws JDependException {
		this.serviceProxy.addAnalyseListener(listener);
	}
}
