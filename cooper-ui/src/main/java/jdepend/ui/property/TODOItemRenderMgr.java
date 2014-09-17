package jdepend.ui.property;

import java.util.HashMap;
import java.util.Map;

import jdepend.util.todolist.RelationData;

public class TODOItemRenderMgr {

	private Map<Class, TODOItemRender> renders = new HashMap<Class, TODOItemRender>();

	private static final TODOItemRenderMgr mgr = new TODOItemRenderMgr();

	private TODOItemRenderMgr() {
		this.init();
	}

	public static TODOItemRenderMgr getInstance() {
		return mgr;
	}

	private void init() {
		renders.put(StringBuilder.class, new TXTTODOItemRender());
		renders.put(RelationData.class, new RelationDataTODOItemRender());
	}

	public TODOItemRender getItemRender(Object info) {
		return renders.get(info.getClass());
	}

}
