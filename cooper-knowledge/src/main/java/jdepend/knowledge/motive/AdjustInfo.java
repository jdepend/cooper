package jdepend.knowledge.motive;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jdepend.framework.exception.JDependException;
import jdepend.model.AreaComponent;
import jdepend.model.Component;
import jdepend.model.JDependUnit;
import jdepend.model.JDependUnitMgr;
import jdepend.model.MetricsMgr;
import jdepend.model.result.AnalysisResult;
import jdepend.model.util.JDependUnitByMetricsComparator;

public class AdjustInfo implements Serializable {

	private static final long serialVersionUID = -2623522836755471041L;

	private List<AreaComponent> areaComponents;

	private Map<String, Float> middles;

	private Map<String, Float> mutabilitys;

	private Map<String, Float> stables;

	private final static String Stable = "Stable";
	private final static String Middle = "Middle";
	private final static String Mutability = "Mutability";

	public AdjustInfo() {
		super();
		this.areaComponents = new ArrayList<AreaComponent>();
		this.middles = new HashMap<String, Float>();
		this.mutabilitys = new HashMap<String, Float>();
		this.stables = new HashMap<String, Float>();
	}

	public static AdjustInfo create(AnalysisResult result) {
		AdjustInfo info = new AdjustInfo();
		info.areaComponents = result.getAreaComponents();
		Map<String, Float> stableList = stableSort(result);
		info.mutabilitys = identifyStables(stableList, Mutability);
		info.stables = identifyStables(stableList, Stable);
		info.middles = identifyStables(stableList, Middle);

		info.updateStable(result);

		return info;
	}

	private static Map<String, Float> identifyStables(Map<String, Float> stableSortList, String mode) {
		if (stableSortList.size() < 2) {
			if (mode.equals(Middle)) {
				return stableSortList;
			} else {
				return null;
			}
		} else {
			Map<String, Float> list = new LinkedHashMap<String, Float>();
			int start = 0;
			int end = 0;
			if (mode.equals(Stable)) {
				start = stableSortList.size() / 3 * 2 + 1;
				end = stableSortList.size();
			} else if (mode.equals(Mutability)) {
				start = 0;
				end = stableSortList.size() / 3;
			} else {
				start = stableSortList.size() / 3 + 1;
				end = stableSortList.size() / 3 * 2;
			}

			Iterator<String> it = stableSortList.keySet().iterator();
			int index = 0;
			String key;
			while (it.hasNext() && index < start) {
				it.next();
				index++;
			}
			while (it.hasNext() && index <= end) {
				key = it.next();
				list.put(key, stableSortList.get(key));
				index++;
			}
			return list;
		}
	}

	private static Map<String, Float> stableSort(AnalysisResult result) {
		List<JDependUnit> units = new ArrayList<JDependUnit>(result.getComponents());
		Collections.sort(units, new JDependUnitByMetricsComparator(MetricsMgr.I, false));
		Map<String, Float> list = new LinkedHashMap<String, Float>();
		for (JDependUnit unit : units) {
			list.put(unit.getName(), unit.stability());
		}
		return list;
	}

	public List<AreaComponent> getAreas() {
		return areaComponents;
	}

	public void setAreas(List<AreaComponent> areas) {
		this.areaComponents = areas;
	}

	public Map<String, Float> getMiddles() {
		return middles;
	}

	public void addMiddle(String componentName, Float balance) {
		this.middles.put(componentName, balance);
		JDependUnitMgr.getInstance().getTheComponent(componentName).setSteadyType(Component.MiddleType);
	}

	public void deleteMiddle(String componentName) {
		this.middles.remove(componentName);
	}

	public Map<String, Float> getMutabilitys() {
		return mutabilitys;
	}

	public void addMutability(String componentName, Float balance) {
		this.mutabilitys.put(componentName, balance);
		JDependUnitMgr.getInstance().getTheComponent(componentName).setSteadyType(Component.MutabilityType);
	}

	public void deleteMutability(String componentName) {
		this.mutabilitys.remove(componentName);
	}

	public Map<String, Float> getStables() {
		return stables;
	}

	public void addStable(String componentName, Float balance) {
		this.stables.put(componentName, balance);
		JDependUnitMgr.getInstance().getTheComponent(componentName).setSteadyType(Component.StableType);
	}

	public void deleteStable(String componentName) {
		this.stables.remove(componentName);
	}

	public void deleteAreaComponent(String areaName) {
		for (AreaComponent areaComponent : this.areaComponents) {
			if (areaComponent.getName().equals(areaName)) {
				this.areaComponents.remove(areaComponent);
				break;
			}
		}
	}

	public void addAreaComponent(String areaName, Collection<Component> components) throws JDependException {
		for (AreaComponent areaComponent : this.areaComponents) {
			if (areaComponent.getName().equals(areaName)) {
				throw new JDependException("区域名字已经存在了");
			}
		}

		AreaComponent areaComponent = new AreaComponent(areaName);
		areaComponent.setComponentList(components);
		this.areaComponents.add(areaComponent);

		Collections.sort(this.areaComponents);
	}

	public void addComponent(String areaName, Component component) throws JDependException {
		for (AreaComponent areaComponent : this.areaComponents) {
			if (areaComponent.getName().equals(areaName)) {
				areaComponent.addComponent(component);
			}
		}
	}

	public void deleteComponent(String areaName, String componentName) throws JDependException {
		for (AreaComponent areaComponent : this.areaComponents) {
			if (areaComponent.getName().equals(areaName)) {
				areaComponent.deleteComponent(componentName);
			}
		}
	}

	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		ois.defaultReadObject();
		this.updateStable(JDependUnitMgr.getInstance().getResult());
	}

	public void updateStable(AnalysisResult result) {
		for (String componentName : this.stables.keySet()) {
			result.getTheComponent(componentName).setSteadyType(Component.StableType);
		}
		for (String componentName : this.middles.keySet()) {
			result.getTheComponent(componentName).setSteadyType(Component.MiddleType);
		}
		for (String componentName : this.mutabilitys.keySet()) {
			result.getTheComponent(componentName).setSteadyType(Component.MutabilityType);
		}
	}

	public void updateAreaComponent(AnalysisResult result) {
		List<Component> components = null;
		Component component = null;
		Iterator<String> itComponent = null;
		String componentName = null;
		for (AreaComponent areaComponent : this.areaComponents) {
			components = new ArrayList<Component>();
			itComponent = areaComponent.getComponents().iterator();
			while (itComponent.hasNext()) {
				componentName = itComponent.next();
				component = result.getTheComponent(componentName);
				if (component != null) {
					components.add(component);
				} else {
					itComponent.remove();
				}
			}
			areaComponent.setComponentList(components);
		}
	}
}
