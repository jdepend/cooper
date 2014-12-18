package jdepend.util.analyzer.element.layer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.model.Component;
import jdepend.model.JavaClass;
import jdepend.model.JavaPackage;
import jdepend.model.component.PrefixComponent;

public final class JEELayer extends PrefixComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3007782209744031746L;

	private List<LayerType> layerTypes = new ArrayList<LayerType>();

	private LayerType layerType;

	public JEELayer() {
	}

	public JEELayer(List<LayerInfo> layerInfos) {
		super();

		for (LayerInfo layerInfo : layerInfos) {
			layerTypes.add(new LayerType(layerInfo));
		}
	}

	public JEELayer(String name, LayerType layerType) {
		super(name);

		this.layerType = layerType;
	}

	@Override
	protected List<Component> doList(Collection<JavaPackage> javaPackages) throws JDependException {

		List<Component> layers = new ArrayList<Component>();
		for (LayerType layerType : layerTypes) {
			layers.add(new JEELayer(layerType.getName(), layerType));
		}

		for (JavaPackage javaPackage : javaPackages) {
			for (Component layer : layers) {
				if (((JEELayer) layer).isMember(javaPackage)) {
					for (JavaClass javaClass : javaPackage.getClasses()) {
						layer.addJavaClass(javaClass);
					}
				}
			}
		}

		return layers;
	}

	@Override
	public boolean isMember(JavaPackage javaPackage) {
		return this.layerType.isMember(javaPackage);
	}
}
