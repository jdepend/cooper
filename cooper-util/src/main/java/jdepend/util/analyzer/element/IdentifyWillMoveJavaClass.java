package jdepend.util.analyzer.element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.model.Component;
import jdepend.model.JDependUnit;
import jdepend.model.JavaClass;
import jdepend.model.result.AnalysisResult;
import jdepend.util.analyzer.framework.AbstractAnalyzer;
import jdepend.util.analyzer.framework.Analyzer;

public class IdentifyWillMoveJavaClass extends AbstractAnalyzer {

	private static final long serialVersionUID = -4694725166948491555L;

	public IdentifyWillMoveJavaClass() {
		super("MoveJavaClass分析", Analyzer.Attention, "识别有移动倾向的JavaClass");
	}

	@Override
	protected void doSearch(AnalysisResult result) throws JDependException {

		Collection<Component> units = result.getComponents();

		List<JavaClassMoveInfo> infos = new ArrayList<JavaClassMoveInfo>();

		for (Component unit : units) {
			for (JavaClass javaClass : unit.getClasses()) {
				// Class是孤立的，又不是私有的
				if (javaClass.isAlone() && !javaClass.isPrivateElement()) {
					Collection<JDependUnit> targets = new ArrayList<JDependUnit>();
					// 收集目标组件
					for (JavaClass relationJavaClass : javaClass.getRelationList()) {
						if (!targets.contains(relationJavaClass.getComponent())) {
							targets.add(relationJavaClass.getComponent());
						}
					}
					// 生成建议
					if (targets.size() == 1) {
						JavaClassMoveInfo info = new JavaClassMoveInfo(javaClass, unit.getName(), targets.iterator()
								.next().getName());
						if (!infos.contains(info)) {
							infos.add(info);
						}
					}
				}
			}
		}
		if (infos.size() > 0) {
			this.isPrintTab(false);
			this.print("以下列出的类与源组件中的类没有关系，而与目标组件中的类存在关系：\n");
			for (JavaClassMoveInfo info : infos) {
				this.print(info.toString());
			}
		}
	}

	@Override
	public String getExplain() {
		return "/culture/关注与反模式/moveJavaClass.htm";
	}

	class JavaClassMoveInfo {

		public JavaClass javaClass;
		public String source;
		public String target;

		public JavaClassMoveInfo(JavaClass javaClass, String source, String target) {
			super();
			this.javaClass = javaClass;
			this.source = source;
			this.target = target;
		}

		@Override
		public String toString() {
			return "建议将JavaClass[" + javaClass.getName() + "]从[" + source + "]移动到[" + target + "]中\n";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((javaClass == null) ? 0 : javaClass.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			JavaClassMoveInfo other = (JavaClassMoveInfo) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (javaClass == null) {
				if (other.javaClass != null)
					return false;
			} else if (!javaClass.equals(other.javaClass))
				return false;
			return true;
		}

		private IdentifyWillMoveJavaClass getOuterType() {
			return IdentifyWillMoveJavaClass.this;
		}

	}

}
