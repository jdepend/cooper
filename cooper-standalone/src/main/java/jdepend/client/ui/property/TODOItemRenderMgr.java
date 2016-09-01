package jdepend.client.ui.property;

import java.util.HashMap;
import java.util.Map;

import jdepend.util.todolist.TODORelationData;

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
		renders.put(TODORelationData.class, new RelationDataTODOItemRender());
	}

	public TODOItemRender getItemRender(Object info) {
		return renders.get(info.getClass());
	}

}
