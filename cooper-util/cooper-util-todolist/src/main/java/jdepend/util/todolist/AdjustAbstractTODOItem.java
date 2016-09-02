package jdepend.util.todolist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.metadata.Method;
import jdepend.model.JDependUnit;
import jdepend.model.JavaClassUnit;
import jdepend.model.MetricsMgr;
import jdepend.model.util.JDependUnitByMetricsComparator;

public final class AdjustAbstractTODOItem extends TODOItem {

	private JDependUnit unit;

	public AdjustAbstractTODOItem(JDependUnit unit) {
		super();
		this.unit = unit;
	}

	public JDependUnit getUnit() {
		return unit;
	}

	@Override
	public List<Object> execute() throws TODOListException {
		StringBuilder info = new StringBuilder();
		if (unit.getStability() < 0.5) {
			Collection<JavaClassUnit> abstractnessClasses = new ArrayList<JavaClassUnit>();
			// 抽象程度不够
			List<JavaClassUnit> javaClasses = new ArrayList<JavaClassUnit>(unit.getClasses());
			// 按传入耦合倒序排序
			Collections.sort(javaClasses, new JDependUnitByMetricsComparator(MetricsMgr.CaCoupling, false));
			// 搜索代码行数超过500，方法超过200的JavaClass
			L: for (JavaClassUnit javaClass : javaClasses) {
				if (!javaClass.getJavaClass().isAbstract() && javaClass.getLineCount() > 500) {
					for (Method method : javaClass.getJavaClass().getSelfMethods()) {
						if (method.getSelfLineCount() > 200 && !method.isStatic()) {
							if (!abstractnessClasses.contains(javaClass)) {
								abstractnessClasses.add(javaClass);
								if (abstractnessClasses.size() >= 5) {
									break L;
								}
							}
						}
					}
				}
			}

			if (abstractnessClasses.size() > 0) {
				for (JavaClassUnit javaClass : abstractnessClasses) {
					info.append("建议将[" + javaClass.getName() + "]设计成接口或抽象类，并采用多个子类分散其逻辑\n");
				}
			} else {
				info.append("未识别出需要设计成接口或抽象类的Class\n");
			}
		} else {
			info.append("不必设计过多的接口或抽象类\n");
		}
		List<Object> infos = new ArrayList<Object>();
		infos.add(info);
		return infos;

	}

	@Override
	public String getAccording() {
		return "违反稳定抽象等价原则";
	}

	@Override
	public List<Object> getInfo() {
		try {
			return this.execute();
		} catch (JDependException e) {
			e.printStackTrace();
			return null;
		}
	}

}
