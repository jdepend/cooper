package jdepend.util.analyzer.element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.model.Component;
import jdepend.model.InvokeItem;
import jdepend.model.JavaClass;
import jdepend.model.JavaClassRelationItem;
import jdepend.model.Method;
import jdepend.model.Relation;
import jdepend.model.result.AnalysisResult;
import jdepend.util.analyzer.framework.AbstractAnalyzer;
import jdepend.util.analyzer.framework.Analyzer;

public class DIPPrinciple extends AbstractAnalyzer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 792778276091284397L;
	private transient List<DIPInfo> dipInfos;

	public DIPPrinciple() {
		super("DIP检查", Analyzer.Attention, "识别违反依赖倒置原则的地方");
	}

	protected void doSearch(AnalysisResult result) throws JDependException {
		Collection<Component> components = result.getComponents();
		this.printDIPs(components);
	}

	private void printDIPs(Collection<Component> components) {

		this.dipInfos = new ArrayList<DIPInfo>();

		Iterator<Component> i = components.iterator();
		while (i.hasNext()) {
			collectDIP(i.next());
		}
		for (DIPInfo info : this.dipInfos) {
			this.printTable("current", info.current);
			this.printTable("depend", info.depend);
			this.printTable("dependInterface", info.Interface);
		}

	}

	private void collectDIP(Component component) {

		boolean found;
		for (Relation relation : component.getRelations()) {
			for (JavaClassRelationItem item : relation.getDetail().getItems()) {
				if (item.getType().canAbstraction()) {
					JavaClass current = item.getCurrent();
					JavaClass depend = item.getDepend();
					// 识别组件外依赖的JavaClass是否是抽象的
					if (!depend.isAbstract() && depend.getSupers().size() > 0) {
						found = true;
						L: for (Method method : current.getSelfMethods()) {
							if (method.getReturnClassTypes().contains(depend)) {
								found = false;
								break L;
							}
							for (InvokeItem invokeItem : method.getInvokeItems()) {
								if (invokeItem.getCallee().getArgClassTypes().contains(depend)) {
									found = false;
									break L;
								}
								if (invokeItem.getCallee().getJavaClass().equals(depend)) {
									if (depend.getSelfMethods().contains(invokeItem.getCallee())) {
										found = false;
										break L;
									}
								}
							}
						}
						if (found) {
							this.dipInfos.add(new DIPInfo(current.getName(), depend.getName(), this
									.getSuperNames(depend.getSupers())));
						}
					}
				}
			}
		}
	}

	@Override
	public String getExplain() {
		StringBuilder explain = new StringBuilder();
		explain.append("识别组件间的依赖【属性和参数】关系中，被依赖的类存在接口而依赖了具体类的情况。<br>");
		explain.append("注：建议将该接口进行精化设计（从使用者角度命名），并打包在使用者所在的组件中。<br>");
		return explain.toString();
	}

	private String getSuperNames(Collection<JavaClass> source) {
		StringBuilder buf = new StringBuilder(100);

		for (JavaClass item : source) {
			buf.append(item.getName());
			buf.append("    ");
		}
		return buf.toString();
	}

	class DIPInfo {

		public String current;

		public String depend;

		public String Interface;

		public DIPInfo(String current, String depend, String interface1) {
			super();
			this.current = current;
			this.depend = depend;
			Interface = interface1;
		}

		@Override
		public String toString() {
			return current + "	" + depend + "	" + Interface;
		}

	}

}
