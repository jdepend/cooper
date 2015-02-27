package jdepend.model.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jdepend.model.Component;
import jdepend.model.JavaClass;
import jdepend.model.component.VirtualComponent;

/**
 * 二级组件识别器
 * 
 * @author user
 * 
 */
public class SecondComponentIdentifyer {

	public Collection<Component> identify(Component current) {

		// 按着Ca数量倒序排序，寻找coreClass
		List<CaCountClass> javaClasses = new ArrayList<CaCountClass>();
		Map<JavaClass, CaCountClass> mapJavaClasses = new HashMap<JavaClass, CaCountClass>();
		CaCountClass caClass;
		int caCount;
		for (JavaClass javaClass : current.getClasses()) {
			caCount = 0;
			for (JavaClass javaClass2 : javaClass.getCaList()) {
				if (current.containsClass(javaClass2)) {
					caCount++;
				}
			}
			caClass = new CaCountClass(javaClass, caCount);
			javaClasses.add(caClass);
			mapJavaClasses.put(javaClass, caClass);
		}
		Collections.sort(javaClasses);

		// 以coreClass为中心聚集直接Class形成组件
		Iterator<CaCountClass> it = javaClasses.iterator();
		Collection<Component> components = new ArrayList<Component>();
		VirtualComponent component;
		JavaClass javaClass;
		CaCountClass caClass2;
		CaCountClass caClass3;

		while (it.hasNext()) {
			caClass = it.next();
			if (!caClass.isRelationed()) {
				caClass.setRelationed();
				javaClass = caClass.getJavaClass();
				component = new VirtualComponent(javaClass);
				//一级搜索
				for (JavaClass javaClass2 : javaClass.getCaList()) {
					if (current.containsClass(javaClass2)) {
						caClass2 = mapJavaClasses.get(javaClass2);
						if (!caClass2.isRelationed() && !caClass2.isCore()) {
							component.joinJavaClass(javaClass2);
							caClass2.setRelationed();
							//二级搜索
							for (JavaClass javaClass3 : javaClass2.getCaList()) {
								if (current.containsClass(javaClass3)) {
									caClass3 = mapJavaClasses.get(javaClass3);
									if (!caClass3.isRelationed() && !caClass3.isCore()) {
										component.joinJavaClass(javaClass3);
										caClass3.setRelationed();
									}
								}
							}
						}
					}
				}
				components.add(component);
			}
		}
		return components;
	}

	static class CaCountClass implements Comparable<CaCountClass> {

		private JavaClass javaClass;
		private Integer caCount;

		private boolean relationed;

		public CaCountClass(JavaClass javaClass, Integer caCount) {
			super();
			this.javaClass = javaClass;
			this.caCount = caCount;
			this.relationed = false;
		}

		public JavaClass getJavaClass() {
			return javaClass;
		}

		public boolean isRelationed() {
			return relationed;
		}

		public void setRelationed() {
			this.relationed = true;
		}

		public boolean isCore() {
			return this.caCount >= 5;
		}

		@Override
		public int compareTo(CaCountClass o) {
			return o.caCount.compareTo(this.caCount);
		}

	}

}
