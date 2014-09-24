package jdepend.model.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutorService;

import jdepend.framework.util.ThreadPool;
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

	private Map<String, Element> elements;

	public Collection<Relation> create(final Collection<Component> components) {
		return create(components, components);
	}

	public Collection<Relation> create(Collection<Component> lefts, final Collection<Component> rights) {
		this.init();

		final Collection<Relation> relations = new Vector<Relation>();

		ExecutorService pool = ThreadPool.getPool();

		for (final Component left : lefts) {
			pool.execute(new Runnable() {
				@Override
				public void run() {
					Relation r;
					float intensity = 0;
					for (Component right : rights) {
						intensity = left.calCeCoupling(right);
						if (intensity != 0) {
							r = new Relation();
							r.setCurrent(createElement(left));
							r.setDepend(createElement(right));
							r.setIntensity(intensity);
							relations.add(r);
						}
					}
				}
			});
		}

		ThreadPool.awaitTermination(pool);
		return new ArrayList<Relation>(relations);
	}

	private void init() {
		elements = new HashMap<String, Element>();
	}

	private Element createElement(Component unit) {
		synchronized (elements) {
			Element element = elements.get(unit.getName());
			if (element == null) {
				element = new Element(unit);
				elements.put(unit.getName(), element);
			}
			return element;
		}
	}
}
