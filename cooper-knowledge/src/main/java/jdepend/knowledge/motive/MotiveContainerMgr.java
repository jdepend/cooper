package jdepend.knowledge.motive;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdepend.model.JDependUnitMgr;

public final class MotiveContainerMgr {

	private static MotiveContainerMgr mgr = new MotiveContainerMgr();

	private Map<String, MotiveContainer> motiveContainers = new HashMap<String, MotiveContainer>();

	private MotiveContainerMgr() {

	}

	public static MotiveContainerMgr getInstance() {
		return mgr;
	}

	public MotiveContainer getMotiveContainer(String group, String command) {
		String key = group + "|" + command;
		if (!this.motiveContainers.containsKey(key)) {
			MotiveContainer motiveContainer = new MotiveContainer(group, command);
			JDependUnitMgr.getInstance().addAnalysisResultListener(motiveContainer);
			this.motiveContainers.put(key, motiveContainer);
		}
		return this.motiveContainers.get(key);
	}

	public void removeMotiveContainer(String group, String command) {
		String key = group + "|" + command;
		this.motiveContainers.remove(key);
	}

	public static List<String> getProblemTypes() {
		return Problem.getTypes();
	}

	public static List<String> getReasons() {
		return Reason.getReasons();
	}

}
