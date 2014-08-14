package jdepend.model.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

		this.init();

		final Collection<Relation> relations = new Vector<Relation>();

		ExecutorService pool = Executors.newFixedThreadPool(4);

		for (final Component left : components) {
			pool.execute(new Runnable() {
				@Override
				public void run() {
					Relation r;
					float intensity = 0;
					for (Component right : components) {
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

		pool.shutdown();

		try {
			boolean loop = true;
			do { // 等待所有任务完成
				loop = !pool.awaitTermination(500, TimeUnit.MILLISECONDS);
			} while (loop);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

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
