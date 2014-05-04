package jdepend.knowledge.capacity;

import jdepend.model.result.AnalysisResult;

public final class CapacityMgr {

	private Capacity capacity;

	private CapacityCreatedListener listener;

	private static CapacityMgr inst = new CapacityMgr();

	private CapacityMgr() {

	}

	public static CapacityMgr getInstance() {
		return inst;
	}

	public Capacity getCapacity(AnalysisResult result) {
		if (capacity == null || !capacity.isSame(result)) {
			capacity = new Capacity(result);
			if (listener != null) {
				listener.onCreated(capacity);
			}
		}
		return capacity;
	}

	public void setListener(CapacityCreatedListener listener) {
		this.listener = listener;
	}
}
