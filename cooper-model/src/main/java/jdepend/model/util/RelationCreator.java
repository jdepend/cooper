package jdepend.model.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jdepend.model.Component;
import jdepend.model.Element;
import jdepend.model.Relation;

/**
 * 分析单元关系创建器
 * 
 * @author <b>Abner</b>
 * 
 */
public class RelationCreator {

	private Map<String, Element> elements = new HashMap<String, Element>();

	public Collection<Relation> create(Collection<Component> components) {

		this.init();

		Collection<Relation> relations = new ArrayList<Relation>();
		Relation r;
		float intensity = 0;

		for (Component left : components) {
			for (Component right : components) {
				intensity = left.calCeCoupling(right);
				if (intensity != 0) {
					r = new Relation();
					r.setCurrent(this.createElement(left));
					r.setDepend(this.createElement(right));
					r.setIntensity(intensity);
					relations.add(r);
				}
			}
		}

		return relations;
	}

	private void init() {
		elements = new HashMap<String, Element>();
	}

	private Element createElement(Component unit) {
		Element element = elements.get(unit.getName());
		if (element == null) {
			element = new Element(unit);
			elements.put(unit.getName(), element);
		}
		return element;
	}
}
