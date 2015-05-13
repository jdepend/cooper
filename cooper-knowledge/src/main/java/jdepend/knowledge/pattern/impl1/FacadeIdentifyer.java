package jdepend.knowledge.pattern.impl1;

import java.util.ArrayList;
import java.util.Collection;

import jdepend.knowledge.pattern.PatternInfo;
import jdepend.model.JavaClassUnit;

public class FacadeIdentifyer extends AbstractPatternIdentifyer {

	@Override
	public String getExplain() {
		StringBuilder explain = new StringBuilder();
		explain.append("&nbsp;&nbsp;&nbsp;&nbsp;<strong>门面模式</strong><br>");
		explain
				.append("&nbsp;&nbsp;&nbsp;&nbsp;1、被组件外的类调用的比例超过九成；2、调用的类是所属组件内的比较超过八成；<br><br>");
		return explain.toString();
	}

	@Override
	public Collection<PatternInfo> identify(Collection<JavaClassUnit> javaClasses) {
		Collection<PatternInfo> rtn = new ArrayList<PatternInfo>();

		Float extCaCallScale;
		Float innerCellScale;
		int innerCellCount;

		for (JavaClassUnit javaClass : javaClasses) {
			if (javaClass.getCaList().size() > 0) {
				extCaCallScale = javaClass.getAfferents().size() / javaClass.getCaList().size() * 1F;
				if (extCaCallScale >= 0.9) {
					if (javaClass.getCeList().size() > 0) {
						innerCellCount = javaClass.getCeList().size() - javaClass.getEfferents().size();
						innerCellScale = innerCellCount / javaClass.getCeList().size() * 1F;
						if (innerCellScale >= 0.8) {
							rtn.add(new PatternInfo(javaClass.getJavaClass(), javaClass.getName()));
						}
					}
				}
			}
		}

		return rtn;
	}
}
