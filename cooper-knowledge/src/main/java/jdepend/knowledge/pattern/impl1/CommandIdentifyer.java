package jdepend.knowledge.pattern.impl1;

import java.util.ArrayList;
import java.util.Collection;

import jdepend.knowledge.pattern.PatternInfo;
import jdepend.model.Attribute;
import jdepend.model.InvokeItem;
import jdepend.model.JavaClass;
import jdepend.model.JavaClassUnit;
import jdepend.model.Method;

public class CommandIdentifyer extends AbstractPatternIdentifyer {

	@Override
	public String getExplain() {
		StringBuilder explain = new StringBuilder();
		explain.append("&nbsp;&nbsp;&nbsp;&nbsp;<strong>命令模式</strong><br>");
		explain.append("&nbsp;&nbsp;&nbsp;&nbsp;1、本身是抽象类或接口；2、存在子类，并子类有属性；3、在调用类中的方法调用了子类接口方法；4、该方法没有参数；5、子类的接口方法中使用了子类属性作为参数调用了实际的被调用者。<br><br>");
		return explain.toString();
	}

	@Override
	public Collection<PatternInfo> identify(Collection<JavaClassUnit> javaClasses) {

		Collection<PatternInfo> rtn = new ArrayList<PatternInfo>();
		PatternInfo rtnItem;
		Collection<Attribute> params;
		for (JavaClassUnit javaClass : javaClasses) {
			if (javaClass.getJavaClass().isAbstract()) {
				for (JavaClass subClass : javaClass.getJavaClass().getSubClasses()) {
					params = subClass.getAttributes();
					if (params.size() > 0) {
						for (Method method : subClass.getOverrideMethods()) {
							if (method.getArgumentCount() == 0) {
								for (InvokeItem item : method.getInvokeItems()) {

								}
							}
						}
					}
				}
			}
		}
		return rtn;
	}

}
