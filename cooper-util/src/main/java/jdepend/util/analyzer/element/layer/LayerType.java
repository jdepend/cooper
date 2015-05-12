package jdepend.util.analyzer.element.layer;

import java.util.ArrayList;
import java.util.List;

import jdepend.model.JavaClassUnit;
import jdepend.model.JavaPackage;

public final class LayerType {

	private JavaClassType layerClassType;

	private LayerInfo layerInfo;

	public LayerType(LayerInfo layerInfo) {
		super();

		this.layerInfo = layerInfo;

		List<String> supers = new ArrayList<String>();
		supers.add(layerInfo.superClassName);

		this.layerClassType = new JavaClassType("LayerClassType", supers);
	}

	public String getName() {
		return this.layerInfo.name;
	}

	public boolean isMember(JavaPackage unit) {
		if (unit.getName().endsWith(layerInfo.packageEndsWith)) {
			return true;
		} else {
			for (JavaClassUnit javaClass : unit.getClasses()) {
				if (this.layerClassType.isMember(javaClass)) {
					return true;
				}
			}
		}
		return false;
	}

}
