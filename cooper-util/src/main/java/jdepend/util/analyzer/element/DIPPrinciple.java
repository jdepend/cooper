package jdepend.util.analyzer.element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.model.Component;
import jdepend.model.InvokeItem;
import jdepend.model.JavaClass;
import jdepend.model.JavaClassRelationItem;
import jdepend.model.Method;
import jdepend.model.relationtype.FieldRelation;
import jdepend.model.relationtype.ParamRelation;
import jdepend.model.result.AnalysisResult;
import jdepend.util.analyzer.framework.AbstractAnalyzer;
import jdepend.util.analyzer.framework.Analyzer;

public class DIPPrinciple extends AbstractAnalyzer {

	private transient List<DIPInfo> dipInfos;

	public DIPPrinciple() {
		super("DIP检查", Analyzer.Attention, "依赖倒置原则");
	}

	protected void doSearch(AnalysisResult result) throws JDependException {
		Collection<Component> units = result.getComponents();
		this.printDIPs(units);
	}

	private void printDIPs(Collection<Component> packages) {

		this.dipInfos = new ArrayList<DIPInfo>();

		Iterator<Component> i = packages.iterator();
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

		List<JavaClass> members = new ArrayList<JavaClass>(component.getClasses());

		Collections.sort(members);
		Iterator<JavaClass> memberIter = members.iterator();
		boolean found;
		while (memberIter.hasNext()) {
			JavaClass current = memberIter.next();
			if (!current.isAbstract()) {
				for (JavaClassRelationItem item : current.getCeItems()) {
					if (item.getType() instanceof FieldRelation || item.getType() instanceof ParamRelation) {
						JavaClass depend = item.getDepend();
						// 识别组件外依赖的JavaClass是否是抽象的
						if (!component.containsClass(depend) && !depend.isAbstract() && depend.getSupers().size() > 0) {
							found = true;
							L: for (Method method : current.getSelfMethods()) {
								if (method.getReturnClassTypes().contains(depend)) {
									found = false;
									break L;
								}
								for (InvokeItem invokeItem : method.getInvokeItems()) {
									if (invokeItem.getMethod().getArgClassTypes().contains(depend)) {
										found = false;
										break L;
									}
									if (invokeItem.getMethod().getJavaClass().equals(depend)) {
										if (depend.getSelfMethods().contains(invokeItem.getMethod())) {
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
